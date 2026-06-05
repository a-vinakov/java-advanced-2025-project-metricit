package ru.otus.vinakov.metric.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "number_entity_metric")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NumberEntityMetric extends EntityMetric {

    @Column(name = "actual_value", nullable = false)
    private Double actualValue;

    @Column(name = "target_value", nullable = false)
    private Double targetValue;

    public NumberEntityMetric(Metric metric, String entityKey, EntityEvent event, Double actualValue, Double targetValue) {
        super(metric, entityKey, event);
        this.actualValue = actualValue;
        this.targetValue = targetValue;
    }

    @Override
    public Metric.Category getCategory() {
        return Metric.Category.NUMBER;
    }

    @Override
    protected MetricHistory buildHistory() {
        NumberMetricHistory history = new NumberMetricHistory();
        history.setActualValueNew(actualValue);
        history.setTargetValueNew(targetValue);
        history.setCreated(getEvent().getCreated());
        history.setEvent(getEvent());
        history.setMetric(this);
        return history;
    }
}