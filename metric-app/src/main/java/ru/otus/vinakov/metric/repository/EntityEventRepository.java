package ru.otus.vinakov.metric.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.vinakov.metric.domain.EntityEvent;

@Repository
public interface EntityEventRepository extends CrudRepository<EntityEvent, Long> {
}
