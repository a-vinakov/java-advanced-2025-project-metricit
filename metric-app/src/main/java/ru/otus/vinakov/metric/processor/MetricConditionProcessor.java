package ru.otus.vinakov.metric.processor;

import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.MetricDataObject;
import ru.otus.vinakov.metric.domain.EntityEvent;
import ru.otus.vinakov.metric.domain.EntityMetric;
import ru.otus.vinakov.metric.domain.MetricCondition;
import ru.otus.vinakov.metric.script.ScriptProcessor;
import ru.otus.vinakov.metric.domain.script.ScriptType;

import java.util.*;

@Slf4j
@Component
public class MetricConditionProcessor {

    private final Map<ScriptType, ScriptProcessor> scriptProcessors;

    private static final String LAST_EVENT_PROPERTY = "last";
    private static final String CURRENT_EVENT_PROPERTY = "now";
    private static final String METRIC_PROPERTY = "metric";

    public MetricConditionProcessor(List<ScriptProcessor> processors) {
        this.scriptProcessors = new HashMap<>();
        for (ScriptProcessor processor : processors) {
            scriptProcessors.put(processor.getType(), processor);
        }
    }

    public boolean validate(MetricCondition condition, MetricDataObject mdo) {
        ScriptProcessor processor = scriptProcessors.get(condition.getScriptType());
        String scriptKey = getConditionScriptKey(condition);
        Map<String, Object> scriptParams = new HashMap<>();
        scriptParams.put(CURRENT_EVENT_PROPERTY, mdo.getCurrentEvent().getAttributes());
        scriptParams.put(LAST_EVENT_PROPERTY, mdo.getLastEvent() != null ? mdo.getLastEvent().getAttributes() : null);
        scriptParams.put(METRIC_PROPERTY, mdo.getEntityMetric() != null ? mdo.getEntityMetric() : null);
        try (ScriptProcessor.EvaluableScript script = processor.getScript(scriptKey)
                .orElseGet(() -> processor.buildScript(scriptKey, condition.getConditionScript()))) {
            Value validationResult = script.eval(scriptParams);
            return validationResult != null && validationResult.asBoolean();
        } catch (Exception e) {
            log.error(String.format("Condition script %s failed", condition.getId()), e);
            return false;
        } finally {
            scriptParams.clear();
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> evaluate(MetricCondition condition, EntityEvent currentEvent, EntityEvent lastEvent, EntityMetric metric) {
        ScriptProcessor processor = scriptProcessors.get(condition.getScriptType());
        String scriptKey = getResultScriptKey(condition);
        Map<String, Object> scriptParams = new HashMap<>();
        scriptParams.put(CURRENT_EVENT_PROPERTY, currentEvent.getAttributes());
        scriptParams.put(LAST_EVENT_PROPERTY, lastEvent != null ? lastEvent.getAttributes() : null);
        scriptParams.put(METRIC_PROPERTY, metric);
        try (ScriptProcessor.EvaluableScript script = processor.getScript(scriptKey)
                .orElseGet(() -> processor.buildScript(scriptKey, condition.getResultScript()))) {
            Value result = script.eval(scriptParams);
            return result.as(Map.class);
        } catch (Exception e) {
            log.error(String.format("Evaluation script %s failed", condition.getId()), e);
            return Collections.emptyMap();
        }
    }

    private String getConditionScriptKey(MetricCondition condition) {
        return condition.getId() + ":condition";
    }

    private String getResultScriptKey(MetricCondition condition) {
        return condition.getId() + ":result";
    }

}
