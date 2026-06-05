package ru.otus.vinakov.metric.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.repository.TimeEntityMetricRepository;

import java.sql.Timestamp;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class MetricSleepWakeTask {

    private final TimeEntityMetricRepository timeEntityMetricRepository;
    private final MetricTaskSynchronizer synchronizer;

    @Scheduled(cron = "0 0/5 * * * *")
    public void updateMetrics() {
        Timestamp now = Timestamp.from(Instant.now());
        synchronizer.runMetricScheduler(() -> {
            timeEntityMetricRepository.sleepDownMetrics(now);
            timeEntityMetricRepository.wakeUpMetrics(now);
        });
    }

}
