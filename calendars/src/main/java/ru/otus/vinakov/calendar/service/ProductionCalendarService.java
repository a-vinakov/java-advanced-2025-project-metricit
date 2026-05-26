package ru.otus.vinakov.calendar.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.vinakov.calendar.model.ProductionCalendar;
import ru.otus.vinakov.calendar.model.ProductionCalendarDay;
import ru.otus.vinakov.calendar.repository.ProductionCalendarRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
//@RequiredArgsConstructor
public class ProductionCalendarService {

    private final ProductionCalendarRepository repository;
    private final ProductionCalendarCache productionCalendarCache;

    private final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");

    public ProductionCalendarService(ProductionCalendarRepository repository) {
        this.repository = repository;
        this.productionCalendarCache = new ProductionCalendarCache() {
            @Override
            protected Map<String, List<ProductionCalendarDay>> loadCalendar(String key) {
                return repository.findByKey(key).map(ProductionCalendar::getYears).orElse(Collections.emptyMap());
            }
        };
    }

    /**
     * Получение списка дней для года с учетом кэширования
     */
    public List<ProductionCalendarDay> getDaysForYear(String calendarKey, int year) {
        return productionCalendarCache.getDaysForYear(calendarKey, year);
    }

    /**
     * Удаление дня
     */
    @Transactional
    public void removeDay(String calendarKey, LocalDate date) {
        ProductionCalendar productionCalendar = repository.findByKey(calendarKey)
                .orElseThrow(() -> new IllegalArgumentException("Calendar not found"));

        String yearStr = date.format(YEAR_FORMATTER);
        List<ProductionCalendarDay> daysList = productionCalendar.getYears().get(yearStr);

        if (daysList != null) {
            daysList.removeIf(d -> d.getDate().isEqual(date));
        }

        repository.save(productionCalendar);
        productionCalendarCache.invalidate(calendarKey);
    }

    /**
     * Создание календаря
     */
    @Transactional
    public ProductionCalendar createCalendar(String key, String name, String description) {
        if (repository.existsByKey(key)) {
            throw new IllegalArgumentException("Calendar with key " + key + " already exists");
        }
        ProductionCalendar productionCalendar = new ProductionCalendar(key, name, description);
        ProductionCalendar saved = repository.save(productionCalendar);
        return saved;
    }

    public Optional<ProductionCalendar> getCalendar(String calendarKey) {
        return repository.findByKey(calendarKey);
    }

}
