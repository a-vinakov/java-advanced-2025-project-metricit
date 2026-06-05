package ru.otus.vinakov.metric.dto.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.otus.vinakov.metric.domain.EntityMetric;
import ru.otus.vinakov.metric.domain.MetricHistory;

@Getter
@Setter
@RequiredArgsConstructor
public class EntityMetricWithLastHistoryFrameDTO {

    private final EntityMetric metric;
    private final MetricHistory history;

}
