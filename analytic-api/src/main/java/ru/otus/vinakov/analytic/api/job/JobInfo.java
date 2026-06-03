package ru.otus.vinakov.analytic.api.job;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class JobInfo {

    private String id;
    private Job.Status status;
    private Object result;
    private Instant created;

}
