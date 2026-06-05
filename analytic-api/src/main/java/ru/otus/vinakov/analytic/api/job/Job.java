package ru.otus.vinakov.analytic.api.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {

    public enum Status {
        READY,
        PROCESSING,
        COMPLETED,
        FAILED
    }

    private String id;
    private String functionKey;
    private Map<String, Object> params;
    private Job.Status status;
    private Object result;
    private Instant created;
    private String callbackUrl;

}
