package ru.otus.vinakov.analytic.function;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.analytic.repository.TimeMetricHistoryRepository;
import ru.otus.vinakov.metric.domain.TimeMetric;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AvgDecisionTimeAF implements AnalyticFunction<AvgDecisionTimeAF.Params, Double> {

    @Getter
    @Setter
    public static class Params {
        private String schemaKey;
    }

    private final TimeMetricHistoryRepository repository;

    @Override
    public String getKey() {
        return "avg_decision_time";
    }

    @Override
    public String getName() {
        return "AVG decision time";
    }

    @Override
    public String getDescription() {
        return "Среднее время решения задач";
    }

    @Override
    public Double execute(Params params) {
        return repository.getAvgTimeThroughStoppedMetrics(params.getSchemaKey(), List.of(
                TimeMetric.TIMER.getKey(),
                TimeMetric.DEADLINE.getKey(),
                TimeMetric.STOPWATCH.getKey()));
    }

    @Override
    public Class<? extends Params> getParamsClass() {
        return Params.class;
    }
}
