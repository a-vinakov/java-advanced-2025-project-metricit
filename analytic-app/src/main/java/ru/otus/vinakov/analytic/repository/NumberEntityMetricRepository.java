package ru.otus.vinakov.analytic.repository;

import org.springframework.stereotype.Repository;
import ru.otus.vinakov.metric.domain.NumberEntityMetric;

import java.util.List;

@Repository
public interface NumberEntityMetricRepository extends org.springframework.data.repository.Repository<NumberEntityMetric, Long> {

    List<NumberEntityMetric> findAllByMetricSchemaKeyAndMetricKeyAndActualValueGreaterThanEqual(String schemaKey, String metricKey, Double value);

}
