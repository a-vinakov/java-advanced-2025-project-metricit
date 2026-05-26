package ru.otus.vinakov.calendar.service;

import ru.otus.vinakov.calendar.model.ProductionCalendarDay;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@Slf4j
//@Component
public abstract class ProductionCalendarCache {

    private final ConcurrentHashMap<String, SoftReference<Map<String, List<ProductionCalendarDay>>>> cache = new ConcurrentHashMap<>();

    /**
     * Получение данных по ключу календаря.
     * Реализует логику горячей загрузки:
     * 1. Пытается получить ссылку.
     * 2. Если ссылка "жива" (не GC), возвращает данные.
     * 3. Если ссылка "мертва" (GC почистил), загружает из БД и обновляет кеш.
     */
    public Map<String, List<ProductionCalendarDay>> getOrLoad(String calendarKey) {
        SoftReference<Map<String, List<ProductionCalendarDay>>> ref = cache.get(calendarKey);
        return ref != null && ref.get() != null ? ref.get() : loadAndCache(calendarKey);
    }

    private Map<String, List<ProductionCalendarDay>> loadAndCache(String calendarKey) {
        return cache.compute(calendarKey, (key, existingRef) -> {
            if (existingRef != null && existingRef.get() != null) {
                return existingRef;
            }
            return new SoftReference<>(loadCalendar(calendarKey));
        }).get();
    }

    public void invalidate(String key) {
        cache.remove(key);
//        log.info("Calendar cache invalidated for key: {}", key);
    }

    public boolean hasData(String key) {
        SoftReference<Map<String, List<ProductionCalendarDay>>> ref = cache.get(key);
        return ref != null && ref.get() != null;
    }

    /**
     * Получение конкретных дней в году
     */
    public List<ProductionCalendarDay> getDaysForYear(String calendarKey, int year) {
        String yearStr = String.valueOf(year);
        Map<String, List<ProductionCalendarDay>> days = getOrLoad(calendarKey);
        return days.computeIfAbsent(yearStr, s -> List.of());
    }

    protected abstract Map<String, List<ProductionCalendarDay>> loadCalendar(String key);

}
