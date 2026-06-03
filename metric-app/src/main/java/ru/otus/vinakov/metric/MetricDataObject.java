package ru.otus.vinakov.metric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.otus.vinakov.metric.domain.EntityEvent;
import ru.otus.vinakov.metric.domain.EntityMetric;
import ru.otus.vinakov.metric.domain.Metric;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class MetricDataObject {

    private final Metric metric;
    private final EntityEvent lastEvent;
    private final EntityEvent currentEvent;
    private EntityMetric entityMetric;

}
