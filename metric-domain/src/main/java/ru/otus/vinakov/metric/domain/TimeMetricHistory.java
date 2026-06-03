package ru.otus.vinakov.metric.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "time_metric_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeMetricHistory extends MetricHistory {

    @Column(name = "status_new")
    private String statusNew;

    @Column(name = "sleeping_new")
    private Boolean sleepingNew;

    @Column(name = "activation_date_new")
    private Timestamp activationDateNew;

    @Column(name = "actual_time_new")
    private Long actualTimeNew;

    @Column(name = "target_time_new")
    private Long targetTimeNew;

    @Column(name = "actual_date_new")
    private Timestamp actualDateNew;

    @Column(name = "target_date_new")
    private Timestamp targetDateNew;

}
