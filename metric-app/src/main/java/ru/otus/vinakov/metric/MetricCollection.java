package ru.otus.vinakov.metric;

import lombok.RequiredArgsConstructor;
import ru.otus.vinakov.metric.domain.EntityMetric;
import ru.otus.vinakov.metric.domain.Metric;
import ru.otus.vinakov.metric.domain.MetricHistory;

import java.util.*;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class MetricCollection {

    private final String entityKey;

    private Map<String, EntityMetric> metrics = new HashMap<>();
    //todo доработать, чтобы можно было вытаскивать всю историю счетчиков
    private Map<String, MetricHistory> history = new HashMap<>();

    public boolean hasMetric(String metricKey) {
        return metrics.containsKey(metricKey);
    }

    //todo synchronized?
    public EntityMetric createMetric(Metric metric, Supplier<EntityMetric> metricProvider) {
        if (!hasMetric(metric.getKey())) {
            EntityMetric mutated = metricProvider.get();
            mutated.setEntityKey(entityKey);
            metrics.put(metric.getKey(), mutated);
            return mutated;
        } else {
            //todo rte
            throw new RuntimeException("Metric already exists in collection");
        }
    }

    public Optional<EntityMetric> getMetric(String metricKey) {
        return Optional.ofNullable(metrics.get(metricKey));

    }

    public Collection<EntityMetric> getMetrics() {
        return Collections.unmodifiableCollection(metrics.values());
    }

    public void addMetric(EntityMetric metric) {
        metrics.put(metric.getMetric().getKey(), metric);
    }

    public void addHistory(String metricKey, MetricHistory metricHistory) {
        history.computeIfAbsent(metricKey, key -> metricHistory);
    }

    public Optional<MetricHistory> getHistory(String metricKey) {
        return Optional.ofNullable(history.get(metricKey));
    }

    public void clear() {
        metrics.clear();
        history.clear();
    }

   /* private final String entityKey;
    private final List<EntityMetric> metrics;

    public MetricCollection(String entityKey, List<EntityMetric> metrics) {
        this.entityKey = entityKey;
        this.metrics = metrics;
    }

    public boolean hasMetric(Metric metric) {
        return hasMetric(metric.getKey());
    }

    public boolean hasMetric(String metricKey) {
        return metrics.stream().anyMatch(m -> m.getMetric().getKey().equals(metricKey));
    }

    //todo synchronized?
    public EntityMetric create(Metric metric, Supplier<EntityMetric> metricProvider) {
        if (!hasMetric(metric)) {
            EntityMetric mutated = metricProvider.get();
            mutated.setEntityKey(entityKey);
            metrics.add(mutated);
            return mutated;
        } else {
            //todo rte
            throw new RuntimeException("Metric already exists in collection");
        }
    }

    public Optional<EntityMetric> get(Metric metric) {
        return metrics.stream().filter(m -> m.getMetric().getKey().equals(metric.getKey()))
                .findFirst();
    }

    public List<EntityMetric> getMetrics() {
        return Collections.unmodifiableList(metrics);
    }

    public void clear() {
        metrics.clear();
    }*/

}
