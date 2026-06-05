package ru.otus.vinakov.calendar.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionCalendarDay {

    @Field("date")
    private LocalDate date;

    @Field("type")
    private DayType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductionCalendarDay)) return false;
        ProductionCalendarDay that = (ProductionCalendarDay) o;
        return date != null && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, type);
    }

}
