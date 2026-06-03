package ru.otus.vinakov.metric.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.vinakov.metric.domain.TimeEntityMetric;

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

    public static TimeEntityMetricDTO from(TimeEntityMetric metric, boolean withMetric) {
        TimeEntityMetricDTO dto = new TimeEntityMetricDTO(metric.getId(), metric.getEntityKey(), metric.getStatus(),
                metric.getSleeping(), metric.getActualTime(), metric.getTargetTime(),
                metric.getActualDate(), metric.getTargetDate());
        if (withMetric) {
            dto.setMetric(MetricDTO.from(metric.getMetric(), false, false));
        }
        return dto;
    }

}
