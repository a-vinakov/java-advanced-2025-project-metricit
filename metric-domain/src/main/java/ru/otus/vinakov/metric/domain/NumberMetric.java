package ru.otus.vinakov.metric.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "number_metric")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NumberMetric extends Metric {

    public static final Metric.Type COUNTER = new Metric.Type("COUNTER", "Counter", "Счетчик числовых показателей, может изменяться на опделенную дельту в плюс или минус");
    public static final Metric.Type GAUGE = new Metric.Type("GAUGE", "Gauge", "Счетчик числовых показателей, в который можно устанавливать любое числовое значение");

    @Column(name = "target_value_default")
    private Double targetValueDefault;

    @Column(name = "actual_value_default")
    private Long actualValueDefault;

    @Override
    public Category getCategory() {
        return Category.NUMBER;
    }
}