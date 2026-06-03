package ru.otus.vinakov.jira.plugin.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueMetricEventResponse {

    private String message;
    private LocalDateTime date;

}
