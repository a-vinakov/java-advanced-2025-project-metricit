package ru.otus.vinakov.metric.postprocessor;

import ru.otus.vinakov.metric.domain.EntityEvent;
import ru.otus.vinakov.metric.domain.EntityMetric;

public interface EntityMetricPostprocessor {

    void postprocess(EntityEvent currentEvent, EntityEvent lastEvent, EntityMetric entityMetric);

    boolean validate(EntityEvent currentEvent, EntityEvent lastEvent, EntityMetric entityMetric);
}
