package ru.otus.vinakov.calendar.api.grpc.client;

import io.grpc.Channel;
import ru.otus.vinakov.calendar.api.WorkPeriod;
import ru.otus.vinakov.calendar.api.grpc.GrpcUtils;
import ru.otus.vinakov.calendar.grpc.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public abstract class CalendarCalculationGrpcClient {

    private final CalendarCalculationGrpcServiceGrpc.CalendarCalculationGrpcServiceBlockingStub stub;

    public CalendarCalculationGrpcClient(Channel channel) {
        this.stub = CalendarCalculationGrpcServiceGrpc.newBlockingStub(channel);
    }

    public ZonedDateTime calculateDate(String calendarKey, ZonedDateTime startDate, Long duration, List<WorkPeriod> periods) {
        DateCalculationGrpcResponse response = stub.calculateDate(DateCalculationGrpcRequest.newBuilder()
                .setCalendarKey(calendarKey)
                .setStartDate(GrpcUtils.formatLocalDateTime(startDate.toLocalDateTime()))
                .setTimezone(startDate.getZone().getId())
                .setDuration(duration)
                .addAllPeriods(GrpcUtils.formatWorkPeriods(periods))
                .build());
        LocalDateTime localDateTime = GrpcUtils.parseLocalDateTime(response.getDate());
        return ZonedDateTime.of(localDateTime, ZoneId.of(response.getTimezone()));
    }

    public Long calculateDuration(String calendarKey, ZonedDateTime startDate, ZonedDateTime endDate, List<WorkPeriod> periods) {
        DurationCalculationGrpcResponse response = stub.calculateDuration(DurationCalculationGrpcRequest.newBuilder()
                .setCalendarKey(calendarKey)
                .setStartDate(GrpcUtils.formatLocalDateTime(startDate.toLocalDateTime()))
                .setStartTimezone(startDate.getZone().getId())
                .setEndDate(GrpcUtils.formatLocalDateTime(endDate.toLocalDateTime()))
                .setEndTimezone(endDate.getZone().getId())
                .addAllPeriods(GrpcUtils.formatWorkPeriods(periods))
                .build());
        return response.getDuration();
    }

}
