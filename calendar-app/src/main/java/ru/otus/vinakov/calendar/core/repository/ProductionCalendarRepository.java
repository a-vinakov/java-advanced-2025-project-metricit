package ru.otus.vinakov.calendar.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.vinakov.calendar.core.model.ProductionCalendar;

import java.util.Optional;

@Repository
public interface ProductionCalendarRepository extends MongoRepository<ProductionCalendar, String> {

    // Поиск календаря по уникальному ключу
    Optional<ProductionCalendar> findByKey(String key);

    // Проверка существования календаря по ключу
    boolean existsByKey(String key);

    void deleteByKey(String key);
}
