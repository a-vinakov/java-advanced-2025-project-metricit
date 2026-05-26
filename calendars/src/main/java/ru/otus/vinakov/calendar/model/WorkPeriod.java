package ru.otus.vinakov.calendar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class WorkPeriod {

    private LocalTime startTime;
    private LocalTime endTime;

    // Конвертер из строки "HH:MM"
    public static WorkPeriod fromStrings(String start, String end) {
        return WorkPeriod.builder()
                .startTime(LocalTime.parse(start))
                .endTime(LocalTime.parse(end))
                .build();
    }

    public static WorkPeriod fromNumbers(int startH, int startM, int endH, int endM) {
        return WorkPeriod.builder()
                .startTime(LocalTime.of(startH, startM, 0))
                .endTime(LocalTime.of(endH, endM, 0))
                .build();
    }

}
