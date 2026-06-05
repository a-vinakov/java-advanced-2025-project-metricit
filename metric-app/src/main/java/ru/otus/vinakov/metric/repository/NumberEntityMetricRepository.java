package ru.otus.vinakov.metric.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.vinakov.metric.domain.NumberEntityMetric;

import java.util.List;

@Repository
public interface NumberEntityMetricRepository extends CrudRepository<NumberEntityMetric, Long> {

    @Query("select nem from NumberEntityMetric nem join fetch Metric m on nem.metric.id = m.id")
    List<NumberEntityMetric> findAllByEntityKeyWithMetric(String entityKey);
}
