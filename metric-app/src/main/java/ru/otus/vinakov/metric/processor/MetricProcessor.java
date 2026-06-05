package ru.otus.vinakov.metric.processor;

import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.MetricDataObject;
import ru.otus.vinakov.metric.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MetricProcessor {

    protected final MetricConditionProcessor conditionProcessor;
    protected final Map<Metric.Category, EntityMetricProcessor> entityMetricProcessors;

    public MetricProcessor(MetricConditionProcessor conditionProcessor, List<EntityMetricProcessor> entityMetricProcessors) {
        this.conditionProcessor = conditionProcessor;
        this.entityMetricProcessors = new HashMap<>();
        for (EntityMetricProcessor processor : entityMetricProcessors) {
            this.entityMetricProcessors.put(processor.getCategory(), processor);
        }
    }

    public Optional<EntityMetric> process(MetricDataObject mdo) {
        for (MetricCondition condition : mdo.getMetric().getConditions()) {
            if (conditionProcessor.validate(condition, mdo)) {
                if (mdo.getEntityMetric() == null) {
                    mdo.setEntityMetric(entityMetricProcessors.get(mdo.getMetric().getCategory())
                            .createEntityMetric(mdo.getCurrentEvent(), mdo.getMetric(), mdo.getCurrentEvent().getEntityKey()));
                }
                Map<String, Object> modifiedProperties = conditionProcessor.evaluate(condition, mdo.getCurrentEvent(),
                        mdo.getLastEvent(), mdo.getEntityMetric());
                if (modifiedProperties != null) {
                    entityMetricProcessors.get(mdo.getMetric().getCategory())
                            .modifyEntityMetric(mdo.getEntityMetric(), modifiedProperties);
                }
            }
        }
        return Optional.ofNullable(mdo.getEntityMetric());
    }

}
