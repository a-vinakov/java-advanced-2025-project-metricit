package ru.otus.vinakov.metric.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Period {
    private LocalTime start;
    private LocalTime end;

    public static String toString(Period period) {
        return String.format("%s-%s", period.getStart().toString(), period.getEnd().toString());
    }

    public static Period fromString(String start, String end) {
        //todo regex + validation
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = end.equals("24:00") ? LocalTime.MIDNIGHT : LocalTime.parse(end);
        return new Period(startTime, endTime);
    }
}
