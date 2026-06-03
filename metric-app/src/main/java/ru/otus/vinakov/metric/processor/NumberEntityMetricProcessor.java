package ru.otus.vinakov.metric.processor;

import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.domain.*;

import java.util.Map;

@Component
public class NumberEntityMetricProcessor implements EntityMetricProcessor {

    private static final String ACTUAL_VALUE_PROP = "actualValue";
    private static final String TARGET_VALUE_PROP = "targetValue";

    @Override
    public Metric.Category getCategory() {
        return Metric.Category.NUMBER;
    }

    @Override
    public EntityMetric createEntityMetric(EntityEvent event, Metric metric, String entityKey) {
        NumberMetric numberMetric = (NumberMetric) metric;
        return new NumberEntityMetric(metric, entityKey, event,
                numberMetric.getActualValueDefault() != null ? numberMetric.getActualValueDefault() : 0d,
                numberMetric.getTargetValueDefault() != null ? numberMetric.getTargetValueDefault() : 0d);
    }

    @Override
    public void modifyEntityMetric(EntityMetric metric, Map<String, Object> modifiedProperties) {
        NumberEntityMetric numberMetric = (NumberEntityMetric) metric;
        setMetricProperty(ACTUAL_VALUE_PROP, modifiedProperties, av -> numberMetric.setActualValue((Double) av));
        setMetricProperty(TARGET_VALUE_PROP, modifiedProperties, tv -> numberMetric.setTargetValue((Double) tv));
    }
}
