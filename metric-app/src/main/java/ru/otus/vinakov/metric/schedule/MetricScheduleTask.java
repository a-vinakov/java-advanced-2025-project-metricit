package ru.otus.vinakov.metric.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.domain.TimeMetricSchedule;
import ru.otus.vinakov.metric.calendar.CalendarGrpcClient;
import ru.otus.vinakov.metric.repository.TimeMetricScheduleRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MetricScheduleTask {

    private final CalendarGrpcClient calendarGrpcClient;
    private final TimeMetricScheduleRepository timeMetricScheduleRepository;

    @Scheduled(cron = "0 1-59/5 * * * *")
    public void updateSchedulers() {
        Instant now = Instant.now();
        List<TimeMetricSchedule> schedules = timeMetricScheduleRepository.findAllOutdated(Timestamp.from(now));
        for (TimeMetricSchedule schedule : schedules) {
            if (schedule.getSleepDate().getTime() / 1000 <= now.getEpochSecond()) {
                //set sleep date
            }
            if (schedule.getWakeDate().getTime() / 1000 <= now.getEpochSecond()) {
                //set wakeup date
            }
        }
    }

}
