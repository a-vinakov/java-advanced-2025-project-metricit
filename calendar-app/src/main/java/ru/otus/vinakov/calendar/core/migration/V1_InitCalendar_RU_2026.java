package ru.otus.vinakov.calendar.core.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.vinakov.calendar.core.model.DayType;
import ru.otus.vinakov.calendar.core.model.ProductionCalendar;
import ru.otus.vinakov.calendar.core.model.ProductionCalendarDay;
import ru.otus.vinakov.calendar.core.repository.ProductionCalendarRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ChangeUnit(id = "initial-calendar-ru-2026", order = "001", author = "vinakov")
public class V1_InitCalendar_RU_2026 {

    private final ProductionCalendarRepository calendarRepository;

    @Execution
    public void initCalendar2026() {
        String key = "RU_2026";
        boolean exists = calendarRepository.existsByKey(key);
        if (exists) {
            log.info("Calendar with key '{}' already exists. Skipping migration.", key);
            return;
        }

        log.info("Creating Production Calendar for year 2026 with key: {}", key);
        List<ProductionCalendarDay> holidays = Arrays.asList(
                // Новый год
                new ProductionCalendarDay(LocalDate.of(2026, 1, 1), DayType.WEEKEND),
                new ProductionCalendarDay(LocalDate.of(2026, 1, 2), DayType.WEEKEND),
                new ProductionCalendarDay(LocalDate.of(2026, 1, 3), DayType.WEEKEND),
                new ProductionCalendarDay(LocalDate.of(2026, 1, 4), DayType.WEEKEND),
                new ProductionCalendarDay(LocalDate.of(2026, 1, 5), DayType.WEEKEND),
                new ProductionCalendarDay(LocalDate.of(2026, 1, 6), DayType.WEEKEND),
                new ProductionCalendarDay(LocalDate.of(2026, 1, 7), DayType.WEEKEND),
                new ProductionCalendarDay(LocalDate.of(2026, 1, 8), DayType.WEEKEND),

                // 23 февраля
                new ProductionCalendarDay(LocalDate.of(2026, 2, 23), DayType.WEEKEND),

                // 8 марта
                new ProductionCalendarDay(LocalDate.of(2026, 3, 8), DayType.WEEKEND),

                // 1 мая
                new ProductionCalendarDay(LocalDate.of(2026, 5, 1), DayType.WEEKEND),
                new ProductionCalendarDay(LocalDate.of(2026, 5, 2), DayType.WEEKEND),

                // 9 мая
                new ProductionCalendarDay(LocalDate.of(2026, 5, 9), DayType.WEEKEND),
                new ProductionCalendarDay(LocalDate.of(2026, 5, 10), DayType.WEEKEND),
                new ProductionCalendarDay(LocalDate.of(2026, 5, 11), DayType.WEEKEND),

                // 12 июня
                new ProductionCalendarDay(LocalDate.of(2026, 6, 12), DayType.WEEKEND),

                // 4 ноября (перед ним сокращенный)
                new ProductionCalendarDay(LocalDate.of(2026, 11, 4), DayType.WEEKEND)
        );

        // 2. Создаем сущность Календаря
        ProductionCalendar calendar = new ProductionCalendar(key,
                "Производственный календарь РФ 2026",
                "Официальные выходные и сокращенные дни для 2026 года");
        calendar.getYears().put("2026", holidays);
        ProductionCalendar saved = calendarRepository.save(calendar);
        log.info("Calendar created successfully. ID: {}, Key: {}, Days count: {}",
                saved.getId(), saved.getKey(), saved.getYears().get("2026").size());
    }

    @RollbackExecution
    public void rollback() {
        calendarRepository.deleteByKey("RU_2026");
    }

}
