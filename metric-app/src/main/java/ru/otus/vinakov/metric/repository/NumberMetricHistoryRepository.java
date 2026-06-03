package ru.otus.vinakov.metric.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.vinakov.metric.domain.NumberMetricHistory;

@Repository
public interface NumberMetricHistoryRepository extends CrudRepository<NumberMetricHistory, Long> {

}
