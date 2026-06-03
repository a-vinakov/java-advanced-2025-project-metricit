package ru.otus.vinakov.metric.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "time_metric")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeMetric extends Metric {

    @Getter
    @AllArgsConstructor
    public static class Status {
        private final String key;
        private final String name;
        private final String description;
        private boolean active;
    }

    public static final Metric.Type TIMER = new Metric.Type("TIMER", "Таймер", "Режим таймера: отводится определенное кол-во времени, и счетчик должен завершить свою работу за отведенное время");
    public static final Metric.Type DEADLINE = new Metric.Type("DEADLINE", "Дедлайн", "Режим дедлайна: счетчику отводится конечная дата завершения, он отсчитывает пройденное время, но при рассчете оставшегося времени опирается на deadline");
    public static final Metric.Type STOPWATCH = new Type("STOPWATCH", "Секундомер", "Режим секундомера: у счетчика нет заранее отведенного времени, он просто отсчитывает кол-во пройденного времени");

    public static final TimeMetric.Status START = new TimeMetric.Status("START", "Start", "Активный статус счетчиков", true);
    public static final TimeMetric.Status STOP = new TimeMetric.Status("STOP", "Stop", "Неактивный статус счетчика", false);

    @Column(name = "target_time_default")
    private Long targetTimeDefault;

    @Column(name = "target_date_default")
    private Timestamp targetDateDefault;

    @Column(name = "type")
    private String type;

    //todo переделать на lazy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id")
    private TimeMetricSchedule schedule;

    @Override
    public Category getCategory() {
        return Category.TIME;
    }
}
