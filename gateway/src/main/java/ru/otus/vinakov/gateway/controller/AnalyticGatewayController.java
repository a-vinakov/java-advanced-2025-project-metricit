package ru.otus.vinakov.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import ru.otus.vinakov.analytic.api.job.Job;
import ru.otus.vinakov.analytic.api.job.JobInfo;
import ru.otus.vinakov.analytic.api.job.JobRequest;
import ru.otus.vinakov.analytic.api.job.JobResponse;
import ru.otus.vinakov.gateway.dto.AnalyticFunctionRequestDTO;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@Tag(name = "Analytic operations")
public class AnalyticGatewayController {

    public record JobEvent(String jobId, Job.Status status, Object result) {
        public static JobEvent processing(String jobId) {
            return new JobEvent(jobId, Job.Status.PROCESSING, null);
        }

        public static JobEvent failed(String jobId) {
            return new JobEvent(jobId, Job.Status.FAILED, null);
        }
    }

    private final WebClient analyticClient;
    private final ConcurrentHashMap<String, Sinks.One<JobEvent>> activeJobs = new ConcurrentHashMap<>();

    public AnalyticGatewayController(@Qualifier("analyticWebClient") WebClient analyticClient) {
        this.analyticClient = analyticClient;
    }

    /**
     * 1. Принимает запрос → отправляет в analytic → получает jobId → открывает SSE с heartbeat
     */
    @Operation(summary = "Receive metric analytic requests")
    @PostMapping(value = "rest/api/analytic", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiResponses({@ApiResponse(description = "Returns analytic data (number, entities or metrics), processed by analytic function", responseCode = "200")})
    public Flux<ServerSentEvent<JobEvent>> analyze(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Analytic functions parameters",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JobEvent.class),
                            examples = @ExampleObject(
                                    name = "Result of AVG decision time function",
                                    value = "{\"jobId\":\"3af4a7fd-eafd-4c25-80be-71e9bccdbc9b\",\"status\":\"COMPLETED\",\"result\":10.0}"
                            )
                    )
            )
            @RequestBody AnalyticFunctionRequestDTO request) {
        return analyticClient.post()
                .uri("/rest/analytic/job")
                .bodyValue(new JobRequest(request.getFunctionKey(), request.getParams(), "/rest/api/analytic/callback"))
                .retrieve()
                .bodyToMono(JobResponse.class)
                .timeout(Duration.ofSeconds(10)) // Защита от зависшей отправки
                .flatMapMany(response -> {
                    String jobId = response.getJobId();
                    log.info("Job {} created. Opening SSE stream.", jobId);
                    Sinks.One<JobEvent> sink = Sinks.one();
                    activeJobs.put(jobId, sink);
                    //Heartbeat каждые 15 сек
                    Flux<ServerSentEvent<JobEvent>> heartbeats = Flux.interval(Duration.ZERO, Duration.ofSeconds(15))
                            .map(tick -> ServerSentEvent.builder(JobEvent.processing(jobId))
                                    .comment("heartbeat")
                                    .build());

                    Flux<ServerSentEvent<JobEvent>> resultStream = sink.asMono()
                            .timeout(Duration.ofMinutes(5), Mono.just(JobEvent.failed(jobId)))
                            .map(event ->
                                    ServerSentEvent.builder(event)
                                            .event(event.status().name())
                                            .build())
                            .flux();
                    //Объединяем heartbeat + результат
                    return Flux.merge(heartbeats, resultStream)
                            .takeUntil(sse -> {
                                JobEvent data = sse.data();
                                return data != null && (data.status() == Job.Status.COMPLETED || data.status() == Job.Status.FAILED);
                            })
                            .doFinally(signal -> {
                                activeJobs.remove(jobId);
                                log.info("SSE closed for job {}. Reason: {}", jobId, signal);
                            });
                })
                .onErrorResume(e -> Flux.just(ServerSentEvent.builder(
                        new JobEvent(null, Job.Status.FAILED, "Submission failed: " + e.getMessage())
                ).build()));
    }

    /**
     * 2. Callback-эндпоинт. Вызывается analytic при завершении задачи
     */
    //todo переделать, так как это должно быть private api
    @PostMapping("/rest/api/analytic/callback")
    public Mono<Void> receiveCallback(@RequestBody JobInfo job) {
        Sinks.One<JobEvent> sink = activeJobs.get(job.getId());
        if (sink != null) {
            Sinks.EmitResult result = sink.tryEmitValue(new JobEvent(job.getId(), job.getStatus(), job.getResult()));
            if (result.isSuccess()) {
                log.info("Callback processed for job {}", job.getId());
            } else {
                log.warn(" Duplicate callback for job {} ignored (sink already terminated)", job.getId());
            }
        } else {
            log.warn("Callback for job {} received, but no active listener", job.getId());
        }
        return Mono.empty();
    }

}
