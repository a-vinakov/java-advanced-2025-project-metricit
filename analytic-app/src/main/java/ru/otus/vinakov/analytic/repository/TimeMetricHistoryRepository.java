package ru.otus.vinakov.analytic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.vinakov.metric.domain.TimeMetricHistory;

import java.util.Collection;

@Repository
public interface TimeMetricHistoryRepository extends org.springframework.data.repository.Repository<TimeMetricHistory, Long> {

    @Query(value = "select avg(tem.actualTime) from TimeEntityMetric tem join Metric m on tem.metric.id = m.id where m.type in :types")
    Double getAvgTimeThroughStoppedMetrics(String schemaKey, @Param("types") Collection<String> types);

}
