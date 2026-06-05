package ru.otus.vinakov.jira.plugin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("conditions")
public class MetricDTO {

    public enum Category {
        NUMBER,
        TIME
    }

    private Long id;
    private String key;
    private String name;
    private Integer index;
    private String type;
    private Category category;
    private MetricSchemaDTO schema;

    public MetricDTO(Long id, String key, String name, Integer index, String type, Category category) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.index = index;
        this.type = type;
        this.category = category;
    }
}
