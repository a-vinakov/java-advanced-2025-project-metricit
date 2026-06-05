package ru.otus.vinakov.metric.processor;

import ru.otus.vinakov.metric.domain.EntityEvent;
import ru.otus.vinakov.metric.domain.EntityMetric;
import ru.otus.vinakov.metric.domain.Metric;

import java.util.Map;
import java.util.function.Consumer;

public interface EntityMetricProcessor {

    Metric.Category getCategory();

    EntityMetric createEntityMetric(EntityEvent event, Metric metric, String entityKey);

    void modifyEntityMetric(EntityMetric metric, Map<String, Object> modifiedProperties);

    default void setMetricProperty(String property, Map<String, Object> properties, Consumer<Object> consumer) {
        if (properties.containsKey(property)) {
            consumer.accept(properties.get(property));
        }
    }
}
