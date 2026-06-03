package ru.otus.vinakov.metric.postprocessor;

import ru.otus.vinakov.metric.domain.EntityEvent;
import ru.otus.vinakov.metric.domain.EntityMetric;
import ru.otus.vinakov.metric.domain.NumberEntityMetric;

public abstract class NumberEntityMetricPostprocessor implements EntityMetricPostprocessor {

    @Override
    public void postprocess(EntityEvent currentEvent, EntityEvent lastEvent, EntityMetric entityMetric) {
        doPostprocess(currentEvent, lastEvent, (NumberEntityMetric) entityMetric);
    }

    @Override
    public boolean validate(EntityEvent currentEvent, EntityEvent lastEvent, EntityMetric entityMetric) {
        return entityMetric instanceof NumberEntityMetric;
    }

    protected abstract void doPostprocess(EntityEvent currentEvent, EntityEvent lastEvent, EntityMetric entityMetric);

}
