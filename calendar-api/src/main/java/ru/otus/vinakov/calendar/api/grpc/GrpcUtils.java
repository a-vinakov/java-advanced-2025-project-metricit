package ru.otus.vinakov.calendar.api.grpc;

import ru.otus.vinakov.calendar.api.WorkPeriod;
import ru.otus.vinakov.calendar.grpc.LocalDateTimeMessage;
import ru.otus.vinakov.calendar.grpc.WorkPeriodMessage;

import java.time.LocalDateTime;
import java.util.List;

public class GrpcUtils {

    public static LocalDateTime parseLocalDateTime(LocalDateTimeMessage dateTime) {
        return LocalDateTime.of(
                dateTime.getYear(),
                dateTime.getMonth(),
                dateTime.getDay(),
                dateTime.getHour(),
                dateTime.getMinute(),
                dateTime.getSecond(),
                0);
    }

    public static LocalDateTimeMessage formatLocalDateTime(LocalDateTime date) {
        return LocalDateTimeMessage.newBuilder()
                .setYear(date.getYear())
                .setMonth(date.getMonth().getValue())
                .setDay(date.getDayOfMonth())
                .setHour(date.getHour())
                .setMinute(date.getMinute())
                .setSecond(date.getSecond())
                .build();
    }

    public static List<WorkPeriod> parseWorkPeriods(List<WorkPeriodMessage> workPeriods) {
        return workPeriods.stream().map(p -> WorkPeriod.fromNumbers(
                p.getStartHour(), p.getStartMinute(), p.getEndHour(), p.getEndMinute()
        )).toList();
    }

    public static List<WorkPeriodMessage> formatWorkPeriods(List<WorkPeriod> periods) {
        return periods.stream().map(p -> WorkPeriodMessage.newBuilder()
                .setStartHour(p.getStartTime().getHour())
                .setStartMinute(p.getStartTime().getMinute())
                .setEndHour(p.getEndTime().getHour())
                .setEndMinute(p.getEndTime().getMinute())
                .build()).toList();
    }

}
