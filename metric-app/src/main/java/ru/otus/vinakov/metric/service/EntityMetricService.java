package ru.otus.vinakov.metric.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.calendar.api.WorkPeriod;
import ru.otus.vinakov.metric.calendar.CalendarGrpcClient;
import ru.otus.vinakov.metric.domain.*;
import ru.otus.vinakov.metric.repository.EntityMetricRepository;
import ru.otus.vinakov.metric.repository.NumberEntityMetricRepository;
import ru.otus.vinakov.metric.repository.TimeEntityMetricRepository;
import ru.otus.vinakov.metric.repository.TimeMetricScheduleRepository;
import ru.otus.vinakov.metric.utils.DateTimeUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EntityMetricService {

    private final EntityMetricRepository entityMetricRepository;
    private final NumberEntityMetricRepository numberEntityMetricRepository;
    private final TimeEntityMetricRepository timeEntityMetricRepository;
    private final TimeMetricScheduleRepository scheduleRepository;
    private final CalendarGrpcClient calendarGrpcClient;

    public List<EntityMetric> getAllMetricsByEntityKey(String entityKey) {
        return entityMetricRepository.findAllByEntityKey(entityKey).stream()
                .peek(em -> {
                    if (em.getCategory() == Metric.Category.TIME) {
                        TimeEntityMetric tem = (TimeEntityMetric) em;
                        TimeMetricSchedule schedule = scheduleRepository.findByTimeEntityMetricId(tem.getId());
                        tem.setActualTime(calculateActualTime(tem, schedule));
                    }
                }).toList();
    }

    public List<NumberEntityMetric> getAllNumberMetricsByEntityKey(String entityKey) {
        return numberEntityMetricRepository.findAllByEntityKeyWithMetric(entityKey);
    }

    public List<TimeEntityMetric> getAllTimeMetricsByEntityKey(String entityKey) {
        return timeEntityMetricRepository.findAllByEntityKeyWithMetric(entityKey).stream()
                .peek(em -> {
                    if (em.getCategory() == Metric.Category.TIME) {
                        TimeMetricSchedule schedule = scheduleRepository.findByTimeEntityMetricId(em.getId());
                        em.setActualTime(calculateActualTime(em, schedule));
                    }
                }).toList();
    }

    public void saveAll(Collection<EntityMetric> entityMetrics) {
        entityMetricRepository.saveAll(entityMetrics);
    }

    private Long calculateActualTime(TimeEntityMetric tem, TimeMetricSchedule schedule) {
        if (tem.getStatus().equals("START") && tem.getActivationDate() != null) {
            return tem.getActualTime() + calendarGrpcClient.calculateDuration(schedule.getCalendarKey(),
                    DateTimeUtils.timestampToZonedDateTime(tem.getActivationDate()),
                    DateTimeUtils.timestampToZonedDateTime(Timestamp.from(Instant.now())),
                    schedule.getPeriods().stream().map(p -> new WorkPeriod(p.getStart(), p.getEnd())).toList());
        } else {
            return tem.getActualTime();
        }
    }
}
