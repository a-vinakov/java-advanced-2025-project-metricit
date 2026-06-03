package ru.otus.vinakov.metric.postprocessor;

import ru.otus.vinakov.metric.domain.EntityEvent;
import ru.otus.vinakov.metric.domain.EntityMetric;
import ru.otus.vinakov.metric.domain.TimeEntityMetric;

public abstract class TimeEntityMetricPostprocessor implements EntityMetricPostprocessor  {

    @Override
    public void postprocess(EntityEvent currentEvent, EntityEvent lastEvent, EntityMetric entityMetric) {
        doPostprocess(currentEvent, lastEvent, (TimeEntityMetric) entityMetric);
    }

    @Override
    public boolean validate(EntityEvent currentEvent, EntityEvent lastEvent, EntityMetric entityMetric) {
        return entityMetric instanceof TimeEntityMetric;
    }

    protected abstract void doPostprocess(EntityEvent currentEvent, EntityEvent lastEvent, TimeEntityMetric entityMetric);
}
