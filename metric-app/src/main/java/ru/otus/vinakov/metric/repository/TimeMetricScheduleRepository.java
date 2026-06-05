package ru.otus.vinakov.metric.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.vinakov.metric.domain.TimeMetricSchedule;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TimeMetricScheduleRepository extends CrudRepository<TimeMetricSchedule, Long> {

    @Query("select tms from TimeMetricSchedule tms where tms.sleepDate <= :now or tms.wakeDate <= :now" )
    List<TimeMetricSchedule> findAllOutdated(Timestamp now);

    TimeMetricSchedule findByKey(String key);

    @Query(value = """
            select tms.* from time_entity_metric tem 
                join entity_metric em on tem.id = em.id 
                join metric m on em.metric_id = m.id 
                join time_metric tm on tm.id = m.id
                join time_metric_schedule tms on tm.schedule_id = tms.id 
                where em.id = :id     
         """, nativeQuery = true)
    TimeMetricSchedule findByTimeEntityMetricId(Long id);

}
