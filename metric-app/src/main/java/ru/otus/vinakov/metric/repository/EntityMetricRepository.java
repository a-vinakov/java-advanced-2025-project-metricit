package ru.otus.vinakov.metric.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.vinakov.metric.domain.EntityMetric;
import ru.otus.vinakov.metric.domain.Metric;
import ru.otus.vinakov.metric.dto.domain.EntityMetricWithLastHistoryFrameDTO;

import java.util.List;

@Repository
public interface EntityMetricRepository extends CrudRepository<EntityMetric, Long> {

    @EntityGraph(attributePaths = "metric")
    List<EntityMetric> findAllByEntityKey(String entityKey);

    @Query("""
                SELECT new ru.otus.vinakov.metric.dto.domain.EntityMetricWithLastHistoryFrameDTO(em, mh)
                FROM EntityMetric em
                LEFT JOIN MetricHistory mh on mh.metric.id = em.id
                AND mh.created = (SELECT max(mh2.created) FROM MetricHistory mh2 WHERE mh2.metric.id = em.metric.id)
            """)
    List<EntityMetricWithLastHistoryFrameDTO> findAllWithLastHistoryFrame(String entityKey);

}
