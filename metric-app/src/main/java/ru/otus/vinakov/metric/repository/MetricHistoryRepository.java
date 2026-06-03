package ru.otus.vinakov.metric.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.vinakov.metric.domain.MetricHistory;
import ru.otus.vinakov.metric.dto.domain.MetricHistoryWithMetricKeyDTO;

import java.util.List;

@Repository
public interface MetricHistoryRepository extends CrudRepository<MetricHistory, Long> {

    @Query("""
        select new ru.otus.vinakov.metric.dto.domain.MetricHistoryWithMetricKeyDTO(m.key, mh)
            from MetricHistory mh
            join EntityMetric em on mh.metric = em
            join Metric m on em.metric = m
            where em.entityKey = :entityKey
            and mh.created = (select max(mh2.created) from MetricHistory mh2 where mh2.metric = em)
    """)
    List<MetricHistoryWithMetricKeyDTO> findAllLatestByEntityKey(String entityKey);

}
