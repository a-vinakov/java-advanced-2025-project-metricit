package ru.otus.vinakov.analytic.api.job;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    private String key;
    private Map<String, Object> params;
    private String callbackUrl;

}
