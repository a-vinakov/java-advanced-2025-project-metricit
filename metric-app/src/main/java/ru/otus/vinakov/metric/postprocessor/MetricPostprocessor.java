package ru.otus.vinakov.metric.postprocessor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.domain.EntityEvent;
import ru.otus.vinakov.metric.domain.EntityMetric;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MetricPostprocessor {

    private final List<EntityMetricPostprocessor> postprocessors;

    public void postprocess(EntityMetric metric, EntityEvent currentEvent, EntityEvent lastEvent) {
        for (EntityMetricPostprocessor postprocessor : postprocessors) {
            if (postprocessor.validate(currentEvent, lastEvent, metric)) {
                postprocessor.postprocess(currentEvent, lastEvent, metric);
            }
        }
    }

}
