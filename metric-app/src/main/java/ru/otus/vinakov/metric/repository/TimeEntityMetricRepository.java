package ru.otus.vinakov.metric.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.vinakov.metric.domain.TimeEntityMetric;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TimeEntityMetricRepository extends CrudRepository<TimeEntityMetric, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            update time_entity_metric ttt
                set sleeping = true,
                actual_time = case
                    when ttt.activation_date is not null then ttt.actual_time + EXTRACT(EPOCH FROM (:now - ttt.activation_date))
                    else ttt.actual_time
                end,
                activation_date = null
                where id in (
                    select tem.id from time_entity_metric tem
                    join entity_metric em on tem.id = em.id
                    join metric m on em.metric_id = m.id
                    join time_metric tm on m.id = tm.id
                    join time_metric_schedule tes on tm.schedule_id = tes.id
                    where tem.status = 'START'
                        and EXTRACT(EPOCH FROM (:now - tes.sleep_date)) < 1
                )""", nativeQuery = true)
    void sleepDownMetrics(Timestamp now);

    @Modifying
    @Transactional
    @Query(value = """
            update time_entity_metric set sleeping = false where id in (
                    select tem.id from time_entity_metric tem
                    join entity_metric em on tem.id = em.id
                    join metric m on em.metric_id = m.id
                    join time_metric tm on m.id = tm.id
                    join time_metric_schedule tes on tm.schedule_id = tes.id
                    where EXTRACT(EPOCH FROM (:now - tes.wake_date)) <= 1
                )""", nativeQuery = true)
    void wakeUpMetrics(Timestamp now);

    @Query("select tem from TimeEntityMetric tem join fetch Metric m on tem.metric.id = m.id")
    List<TimeEntityMetric> findAllByEntityKeyWithMetric(String entityKey);

}
