package ru.otus.vinakov.metric.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "time_entity_metric")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeEntityMetric extends EntityMetric {

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "sleeping", nullable = false)
    private Boolean sleeping;

    @Column(name = "activation_date")
    private Timestamp activationDate;

    @Column(name = "actual_time")
    private Long actualTime;

    @Column(name = "target_time")
    private Long targetTime;

    @Column(name = "actual_date")
    private Timestamp actualDate;

    @Column(name = "target_date")
    private Timestamp targetDate;

    public TimeEntityMetric(Metric metric, String entityKey, EntityEvent event, String status, Boolean sleeping,
                            Timestamp activationDate, Long actualTime, Long targetTime, Timestamp actualDate, Timestamp targetDate) {
        super(metric, entityKey, event);
        this.status = status;
        this.sleeping = sleeping;
        this.activationDate = activationDate;
        this.actualTime = actualTime;
        this.targetTime = targetTime;
        this.actualDate = actualDate;
        this.targetDate = targetDate;
    }

    @Override
    public Metric.Category getCategory() {
        return Metric.Category.TIME;
    }

    @Override
    protected MetricHistory buildHistory() {
        TimeMetricHistory history = new TimeMetricHistory();
        history.setStatusNew(getStatus());
        history.setSleepingNew(getSleeping());
        history.setActivationDateNew(getActivationDate());
        history.setActualTimeNew(getActualTime());
        history.setTargetTimeNew(getTargetTime());
        history.setActualDateNew(getActualDate());
        history.setTargetDateNew(getTargetDate());
        //todo fix
        history.setCreated(getEvent() != null ? getEvent().getCreated() : Timestamp.from(Instant.now()));
        history.setEvent(getEvent());
        history.setMetric(this);
        return history;
    }
}