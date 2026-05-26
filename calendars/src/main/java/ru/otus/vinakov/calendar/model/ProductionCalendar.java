package ru.otus.vinakov.calendar.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.*;

@Data
@Document(collection = "production_calendars")
public class ProductionCalendar {

    @Id
    private String id;

    @Field("key")
    @Indexed(unique = true)
    private String key;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("years")
    private Map<String, List<ProductionCalendarDay>> years;

    public ProductionCalendar(String key, String name, String description) {
        this.key = key;
        this.name = name;
        this.description = description;
        this.years = new HashMap<>();
    }

    public List<ProductionCalendarDay> getDaysOfYear(String year) {
        return years.getOrDefault(year, new ArrayList<>());
    }

    public void putDays(String year, List<ProductionCalendarDay> days) {
        years.put(year, days);
    }

    public DayType getDayType(LocalDate date) {
        String yearStr = String.valueOf(date.getYear());
        return years.getOrDefault(yearStr, Collections.emptyList()).stream()
                .filter(d -> d.getDate().equals(date))
                .findFirst()
                .map(ProductionCalendarDay::getType)
                .orElse(null);
    }

    public ProductionCalendarDay getDay(LocalDate date) {
        String yearStr = String.valueOf(date.getYear());
        if (years.containsKey(yearStr)) {
            List<ProductionCalendarDay> days = years.get(yearStr);
            for (ProductionCalendarDay day : days) {
                if (date.equals(day.getDate())) {
                    return day;
                }
            }
        }
        return null;
    }

}
