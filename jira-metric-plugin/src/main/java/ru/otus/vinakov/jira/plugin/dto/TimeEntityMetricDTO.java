package ru.otus.vinakov.jira.plugin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class TimeEntityMetricDTO extends EntityMetricDTO {

    private String status;
    private boolean sleeping;
    private Long actualTime;
    private Long targetTime;
    private Timestamp actualDate;
    private Timestamp targetDate;

    public TimeEntityMetricDTO(Long id, String entityKey, String status, boolean sleeping,
                               Long actualTime, Long targetTime, Timestamp actualDate, Timestamp targetDate) {
        super(id, entityKey);
        this.status = status;
        this.sleeping = sleeping;
        this.actualTime = actualTime;
        this.targetTime = targetTime;
        this.actualDate = actualDate;
        this.targetDate = targetDate;
    }
}
