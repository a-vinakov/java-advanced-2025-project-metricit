package ru.otus.vinakov.calendar.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.vinakov.calendar.api.WorkPeriod;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class DatePoint {

    public enum Type {
        START_OF_DAY,
        END_OF_DAY,
        START_OF_WP,
        END_OF_WP
    }

    private final int offset;
    private final Type type;

    public DatePoint(LocalTime time, Type type) {
        this.offset = time.get(ChronoField.SECOND_OF_DAY);
        this.type = type;
    }

    public static final DatePoint START_OF_DAY = new DatePoint(0, Type.START_OF_DAY);
    public static final DatePoint END_OF_DAY = new DatePoint(24 * 60 * 60, Type.END_OF_DAY);

    public static List<DatePoint> getDayDatePoints(List<WorkPeriod> workPeriods) {
        List<DatePoint> datePoints = new ArrayList<>();
        datePoints.add(DatePoint.START_OF_DAY);
        for (WorkPeriod period : workPeriods) {
            datePoints.add(new DatePoint(period.getStartTime(), DatePoint.Type.START_OF_WP));
            datePoints.add(new DatePoint(period.getEndTime(), DatePoint.Type.END_OF_WP));
        }
        datePoints.add(DatePoint.END_OF_DAY);
        return datePoints;
    }
}
