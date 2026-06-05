package ru.otus.vinakov.analytic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.vinakov.analytic.api.job.JobInfo;
import ru.otus.vinakov.analytic.api.job.JobRequest;
import ru.otus.vinakov.analytic.api.job.JobResponse;
import ru.otus.vinakov.analytic.service.AnalyticJobService;

@RestController
@RequiredArgsConstructor
public class AnalyticJobController {

    private final AnalyticJobService jobService;

    /**
     * Принимает запрос, регистрирует job, возвращает jobId
     */
    @PostMapping("rest/analytic/job")
    public ResponseEntity<JobResponse> createJob(@RequestBody JobRequest request) {
        return ResponseEntity.accepted().body(jobService.submitJob(request));
    }

    /**
     * Возвращает статус и результат по jobId
     */
    @GetMapping("rest/analytic/job/{jobId}")
    public ResponseEntity<JobInfo> getJobStatus(@PathVariable String jobId) {
        JobInfo info = jobService.getJobStatus(jobId);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }
}
