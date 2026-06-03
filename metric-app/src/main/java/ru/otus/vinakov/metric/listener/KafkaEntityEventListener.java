package ru.otus.vinakov.metric.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.MetricCollection;
import ru.otus.vinakov.metric.MetricDataObject;
import ru.otus.vinakov.metric.domain.EntityEvent;
import ru.otus.vinakov.metric.domain.EntityMetric;
import ru.otus.vinakov.metric.domain.Metric;
import ru.otus.vinakov.metric.domain.MetricHistory;
import ru.otus.vinakov.metric.domain.event.KafkaEntityEvent;
import ru.otus.vinakov.metric.postprocessor.MetricPostprocessor;
import ru.otus.vinakov.metric.processor.MetricProcessor;
import ru.otus.vinakov.metric.repository.EntityEventRepository;
import ru.otus.vinakov.metric.schedule.MetricTaskSynchronizer;
import ru.otus.vinakov.metric.service.EntityMetricService;
import ru.otus.vinakov.metric.service.MetricHistoryService;
import ru.otus.vinakov.metric.service.MetricService;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class KafkaEntityEventListener {

    private final EntityEventRepository entityEventRepository;
    private final MetricService metricService;
    private final EntityMetricService entityMetricService;
    private final MetricHistoryService metricHistoryService;
    private final MetricProcessor metricProcessor;
    private final MetricPostprocessor metricPostprocessor;
    private final MetricTaskSynchronizer synchronizer;

    @KafkaListener(topics = "${kafka.metric.topic}")
    public void onEntityEvent(KafkaEntityEvent event) {
        synchronizer.runMetricEventHandler(() -> {
            //0. сохранить event в БД
            EntityEvent entityEvent = entityEventRepository.save(new EntityEvent(event.getEntityKey(),
                    Timestamp.from(event.getEventDate().atZone(ZoneId.of(event.getTimezone())).toInstant()), event.getAttributes()));
            //1. вытащить все метрики из указанной схемы вместе с их condition'ами
            List<Metric> metrics = metricService.getAllMetricsBySchema(event.getSchemaKey());
            //2. вытащить все уже существующие метрики
            MetricCollection metricsCollection = new MetricCollection(event.getEntityKey());
            entityMetricService.getAllMetricsByEntityKey(event.getEntityKey())
                    .forEach(metricsCollection::addMetric);
            //3. вытащить историю по существующим метрикам
            metricHistoryService.getEntityMetricHistory(event.getEntityKey())
                    .forEach(m -> metricsCollection.addHistory(m.getMetricKey(), m.getHistory()));
            //4. нужно обновить все существующие метрики, и создать новые, если требуется
            for (Metric metric : metrics) {
                Optional<MetricHistory> mh = metricsCollection.getHistory(metric.getKey());
                Optional<EntityMetric> em = metricsCollection.getMetric(metric.getKey());
                MetricDataObject mdo = new MetricDataObject(metric, mh.map(MetricHistory::getEvent).orElse(null),
                        entityEvent, em.orElse(null));
                Optional<EntityMetric> entityMetric = metricProcessor.process(mdo);
                entityMetric.ifPresent(m -> metricPostprocessor.postprocess(m, entityEvent,
                        metricsCollection.getHistory(metric.getKey()).map(MetricHistory::getEvent).orElse(null)));
                entityMetric.ifPresent(m -> m.setEvent(entityEvent));
                if (!metricsCollection.hasMetric(metric.getKey()) && mdo.getEntityMetric() != null) {
                    metricsCollection.addMetric(mdo.getEntityMetric());
                }
            }
            //5. сохраняем все в БД
            entityMetricService.saveAll(metricsCollection.getMetrics());
            metricsCollection.clear();
        });
    }

}
