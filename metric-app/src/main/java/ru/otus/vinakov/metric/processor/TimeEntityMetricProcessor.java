package ru.otus.vinakov.metric.processor;

import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.domain.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

@Component
public class TimeEntityMetricProcessor implements EntityMetricProcessor {

    private static final String STATUS_PROP = "status";
    private static final String ACTUAL_TIME_PROP = "actualTime";
    private static final String TARGET_TIME_PROP = "targetTime";
    private static final String ACTUAL_DATE_PROP = "actualDate";
    private static final String TARGET_DATE_PROP = "targetDate";

    @Override
    public Metric.Category getCategory() {
        return Metric.Category.TIME;
    }

    @Override
    public EntityMetric createEntityMetric(EntityEvent event, Metric metric, String entityKey) {
        TimeMetric tm = (TimeMetric) metric;
        return new TimeEntityMetric(metric, entityKey, event,
                TimeMetric.START.getKey(),
                false,
                //todo а если статус STOP?
                event.getCreated(),
                0L, tm.getTargetTimeDefault() != null ? tm.getTargetTimeDefault() : 0L,
                null,
                //Timestamp.from(Instant.now()),
                tm.getTargetDateDefault() != null ? tm.getTargetDateDefault() : null);
    }

    @Override
    public void modifyEntityMetric(EntityMetric metric, Map<String, Object> modifiedProperties) {
        TimeEntityMetric entityMetric = (TimeEntityMetric) metric;
        setMetricProperty(STATUS_PROP, modifiedProperties, s -> entityMetric.setStatus((String) s));
        setMetricProperty(ACTUAL_TIME_PROP, modifiedProperties, at -> entityMetric.setActualTime(Long.valueOf(at.toString())));
        setMetricProperty(TARGET_TIME_PROP, modifiedProperties, tt -> entityMetric.setTargetTime(Long.valueOf(tt.toString())));
        //todo date conversion
//        setMetricProperty(ACTUAL_DATE_PROP, modifiedProperties, ad -> entityMetric.setActualDate((Timestamp) ad));
//        setMetricProperty(TARGET_DATE_PROP, modifiedProperties, td -> entityMetric.setTargetDate((Timestamp) td));
    }
}
