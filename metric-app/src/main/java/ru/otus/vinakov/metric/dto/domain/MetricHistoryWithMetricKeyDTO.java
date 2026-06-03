package ru.otus.vinakov.metric.dto.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.vinakov.metric.domain.MetricHistory;

@Getter
@RequiredArgsConstructor
public class MetricHistoryWithMetricKeyDTO {

    private final String metricKey;
    private final MetricHistory history;

}
