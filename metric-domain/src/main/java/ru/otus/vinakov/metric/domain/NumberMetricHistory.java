package ru.otus.vinakov.metric.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "number_metric_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NumberMetricHistory extends MetricHistory {

    @Column(name = "actual_value_new")
    private Double actualValueNew;

    @Column(name = "target_value_new")
    private Double targetValueNew;

}
