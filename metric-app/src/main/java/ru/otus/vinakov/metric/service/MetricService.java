package ru.otus.vinakov.metric.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.domain.Metric;
import ru.otus.vinakov.metric.repository.MetricRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MetricService {

    private final MetricRepository metricRepository;

    public List<Metric> getAllMetricsBySchema(String schemaKey) {
        return metricRepository.findAllBySchemaKey(schemaKey);
    }
}
