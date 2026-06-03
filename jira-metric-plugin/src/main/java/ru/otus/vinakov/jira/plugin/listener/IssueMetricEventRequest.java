package ru.otus.vinakov.jira.plugin.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IssueMetricEventRequest {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private String schemaKey;
    private String entityKey;
    private String eventDate;
    private String timezone;
    private Map<String, Object> attributes;

    public IssueMetricEventRequest(String schemaKey, String entityKey, Map<String, Object> attributes) {
        this.schemaKey = schemaKey;
        this.entityKey = entityKey;
        this.eventDate = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        //todo убрать
        this.timezone = "GMT+03:00";
        this.attributes = attributes;
    }

}
