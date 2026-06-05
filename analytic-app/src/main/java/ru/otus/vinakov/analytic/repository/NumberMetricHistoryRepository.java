package ru.otus.vinakov.analytic.repository;

import org.springframework.stereotype.Repository;
import ru.otus.vinakov.metric.domain.NumberMetricHistory;

@Repository
public interface NumberMetricHistoryRepository extends org.springframework.data.repository.Repository<NumberMetricHistory, Long> {
}
