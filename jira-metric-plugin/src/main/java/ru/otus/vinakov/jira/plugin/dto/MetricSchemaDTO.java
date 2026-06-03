package ru.otus.vinakov.jira.plugin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetricSchemaDTO {

    private Long id;
    private String key;
    private String name;

}
