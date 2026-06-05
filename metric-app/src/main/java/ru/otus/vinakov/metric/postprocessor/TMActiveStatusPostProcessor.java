package ru.otus.vinakov.metric.postprocessor;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.calendar.api.WorkPeriod;
import ru.otus.vinakov.metric.domain.EntityEvent;
import ru.otus.vinakov.metric.domain.TimeEntityMetric;
import ru.otus.vinakov.metric.domain.TimeMetric;
import ru.otus.vinakov.metric.domain.TimeMetricSchedule;
import ru.otus.vinakov.metric.calendar.CalendarGrpcClient;
import ru.otus.vinakov.metric.repository.TimeMetricScheduleRepository;
import ru.otus.vinakov.metric.utils.DateTimeUtils;

import java.time.*;

@Component
@Order(1)
@RequiredArgsConstructor
public class TMActiveStatusPostProcessor extends TimeEntityMetricPostprocessor {

    private final CalendarGrpcClient calendarClient;
    private final TimeMetricScheduleRepository timeMetricScheduleRepository;

    @Override
    protected void doPostprocess(EntityEvent currentEvent, EntityEvent lastEvent, TimeEntityMetric entityMetric) {
        //todo status graph
        if (entityMetric.getActivationDate() != null) {
            TimeMetric tm = (TimeMetric) entityMetric.getMetric();
            TimeMetricSchedule schedule = timeMetricScheduleRepository.findByKey(tm.getSchedule().getKey());
            if (schedule != null) {
                //счетчик работает по расписанию, лезем в календарь
                Long timePassed = calendarClient.calculateDuration(schedule.getCalendarKey(),
                        DateTimeUtils.timestampToZonedDateTime(entityMetric.getActivationDate(), schedule.getTimezone()),
                        DateTimeUtils.timestampToZonedDateTime(currentEvent.getCreated(), schedule.getTimezone()),
//                        ZonedDateTime.ofInstant(currentEvent.getCreated(), ZoneId.of(schedule.getTimezone())),
                        schedule.getPeriods().stream().map(p -> new WorkPeriod(p.getStart(), p.getEnd())).toList());
                entityMetric.setActualTime(entityMetric.getActualTime() + timePassed);
            } else {
                //счетчик работает без расписания, считаем просто разницу между датами
                Long timePassed = Duration.between(DateTimeUtils.timestampToZonedDateTime(entityMetric.getActivationDate()),
                        DateTimeUtils.timestampToZonedDateTime(currentEvent.getCreated())).getSeconds();
                entityMetric.setActualTime(entityMetric.getActualTime() + timePassed);
            }
        }
        if (entityMetric.getStatus().equals("START")) {
            entityMetric.setActivationDate(currentEvent.getCreated());
        } else {
            entityMetric.setActivationDate(null);
        }
    }
}
