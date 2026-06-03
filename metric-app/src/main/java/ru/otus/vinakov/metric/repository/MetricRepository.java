package ru.otus.vinakov.metric.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.vinakov.metric.domain.Metric;

import java.util.List;

@Repository
public interface MetricRepository extends CrudRepository<Metric, Long> {

    @EntityGraph(attributePaths = "conditions")
    List<Metric> findAllBySchemaKey(String schemaKey);

}
