package ru.otus.vinakov.calendar.grpc.server;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.calendar.grpc.*;
import ru.otus.vinakov.calendar.grpc.GrpcUtils;
import ru.otus.vinakov.calendar.service.DateCalculationService;
import ru.otus.vinakov.calendar.service.DurationCalculationService;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@ConditionalOnProperty(name = "grpc.active", havingValue = "true")
@RequiredArgsConstructor
public class CalendarCalculationGrpcServer extends CalendarCalculationGrpcServiceGrpc.CalendarCalculationGrpcServiceImplBase {

    private final DateCalculationService dateCalculationService;
    private final DurationCalculationService durationCalculationService;

    @Override
    public void calculateDate(DateCalculationGrpcRequest request, StreamObserver<DateCalculationGrpcResponse> responseObserver) {
        ZonedDateTime zonedDateTime = dateCalculationService.calculate(
                request.getCalendarKey(),
                ZonedDateTime.of(GrpcUtils.parseLocalDateTime(request.getStartDate()), ZoneId.of(request.getTimezone())),
                request.getDuration(),
                GrpcUtils.parseWorkPeriods(request.getPeriodsList()));
        responseObserver.onNext(DateCalculationGrpcResponse.newBuilder()
                .setDate(GrpcUtils.formatLocalDateTime(zonedDateTime.toLocalDateTime()))
                .setTimezone(zonedDateTime.getZone().getId())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void calculateDuration(DurationCalculationGrpcRequest request, StreamObserver<DurationCalculationGrpcResponse> responseObserver) {
        Long duration = durationCalculationService.calculate(
                request.getCalendarKey(),
                ZonedDateTime.of(GrpcUtils.parseLocalDateTime(request.getStartDate()), ZoneId.of(request.getStartTimezone())),
                ZonedDateTime.of(GrpcUtils.parseLocalDateTime(request.getEndDate()), ZoneId.of(request.getEndTimezone())),
                GrpcUtils.parseWorkPeriods(request.getPeriodsList())
        );
        responseObserver.onNext(DurationCalculationGrpcResponse.newBuilder()
                .setDuration(duration)
                .build());
        responseObserver.onCompleted();
    }

}
