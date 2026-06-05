package ru.otus.vinakov.analytic.service;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.otus.vinakov.analytic.function.AnalyticFunction;
import ru.otus.vinakov.analytic.function.AnalyticFunctionManager;
import ru.otus.vinakov.analytic.api.job.Job;
import ru.otus.vinakov.analytic.api.job.JobInfo;
import ru.otus.vinakov.analytic.api.job.JobRequest;
import ru.otus.vinakov.analytic.api.job.JobResponse;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class AnalyticJobService {

    private final ConcurrentHashMap<String, Job> store = new ConcurrentHashMap<>();
    private final ThreadPoolExecutor executor;
    private final AnalyticFunctionManager functionManager;
    private final RestClient gatewayRestClient;

    private static class AnalyticThreadFactory implements ThreadFactory {

        private final AtomicInteger count = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "analytic-worker-" + count.getAndIncrement());
        }
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public AnalyticJobService(@Value("${analytic.jobs.max-concurrent}") int maxConcurrent, AnalyticFunctionManager functionManager,
                              @Qualifier("gatewayRestClient") RestClient gatewayRestClient) {
        this.executor = new ThreadPoolExecutor(
                maxConcurrent, maxConcurrent,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new AnalyticThreadFactory());
        this.functionManager = functionManager;
        this.gatewayRestClient = gatewayRestClient;
    }

    /**
     * Регистрация задачи и возврат jobId
     */
    public JobResponse submitJob(JobRequest request) {
        String jobId = UUID.randomUUID().toString();
        Job job = new Job(jobId, request.getKey(), request.getParams(), Job.Status.READY, null, Instant.now(), request.getCallbackUrl());
        store.put(jobId, job);
        executor.submit(() -> executeJob(jobId));
        log.info("Job {} registered and queued", jobId);
        return new JobResponse(jobId);
    }

    /**
     * Получение статуса по jobId
     */
    public JobInfo getJobStatus(String jobId) {
        Job job = store.get(jobId);
        if (job == null) return null;
        return new JobInfo(job.getId(), job.getStatus(), job.getResult(), job.getCreated());
    }

    /**
     * Внутренний процессор задачи
     */
    @SuppressWarnings("unchecked")
    private void executeJob(String jobId) {
        try {
            Job job = store.compute(jobId, (k, j) -> {
                j.setStatus(Job.Status.PROCESSING);
                return j;
            });
            log.info("Started processing job: {}", jobId);
            AnalyticFunction<Object, Object> function = functionManager.getFunction(job.getFunctionKey())
                    .orElseThrow(() -> new RuntimeException("Function with key " + job.getFunctionKey() + " not found"));
            Object params = parseJson(job.getParams(), function.getParamsClass());
            Object result = function.execute(params);
            log.info("Job {} completed", job);

            Job completedJob = store.compute(jobId, (k, j) -> {
                j.setStatus(Job.Status.COMPLETED);
                j.setResult(result);
                return j;
            });
            notifyGateway(new JobInfo(completedJob.getId(), Job.Status.COMPLETED, completedJob.getResult(), Instant.now()));
        } catch (Exception e) {
            log.error("Job {} failed: {}", jobId, e.getMessage(), e);
            Job failedJob = store.compute(jobId, (k, j) -> {
                j.setStatus(Job.Status.FAILED);
                j.setResult(Map.of("error", e.getMessage()));
                return j;
            });
            notifyGateway(new JobInfo(failedJob.getId(), Job.Status.FAILED, failedJob.getResult(), Instant.now()));
        } finally {
            store.remove(jobId);
        }
    }

    //todo нужно заменить эту схему, чтобы gateway сам передавал свой URL для callback
    private void notifyGateway(JobInfo jobInfo) {
        gatewayRestClient.post()
                .uri("/rest/api/analytic/callback")
                .contentType(MediaType.APPLICATION_JSON)
                .body(jobInfo)
                .retrieve()
                .toBodilessEntity();
    }

    private synchronized Object parseJson(Map<String, Object> params, Class<?> paramsClass) {
        //todo костыль, надо переделать
        return MAPPER.readValue(MAPPER.writeValueAsString(params), paramsClass);
    }

    private synchronized String formatJson(Object value) {
        return MAPPER.writeValueAsString(value);
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down analytic executor...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Принудительная остановка, если не уложились в таймаут
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("Analytic executor stopped");
    }


}
