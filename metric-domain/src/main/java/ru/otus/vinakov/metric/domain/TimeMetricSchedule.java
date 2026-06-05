package ru.otus.vinakov.metric.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.otus.vinakov.metric.domain.converter.PeriodListConverter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "time_metric_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeMetricSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "timezone", nullable = false)
    private String timezone;

    @Column(name = "sleep_date")
    private Timestamp sleepDate;

    @Column(name = "wake_date")
    private Timestamp wakeDate;

    @Column(name = "calendar_key", nullable = false)
    private String calendarKey;

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = PeriodListConverter.class)
    @Column(name = "periods", columnDefinition = "jsonb", nullable = false)
    private List<Period> periods;

}
