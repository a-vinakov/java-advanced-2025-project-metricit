package ru.otus.vinakov.metric.calendar;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.calendar.api.WorkPeriod;
import ru.otus.vinakov.calendar.api.grpc.client.CalendarCalculationGrpcClient;

import java.time.ZonedDateTime;
import java.util.List;

@Component
public class CalendarGrpcClient extends CalendarCalculationGrpcClient {

    public CalendarGrpcClient(@Qualifier("calendar") Channel channel) {
        super(channel);
    }

    @Override
    public ZonedDateTime calculateDate(String calendarKey, ZonedDateTime startDate, Long duration, List<WorkPeriod> periods) {
        return super.calculateDate(calendarKey, startDate, duration, periods);
    }

    @Override
    public Long calculateDuration(String calendarKey, ZonedDateTime startDate, ZonedDateTime endDate, List<WorkPeriod> periods) {
        return super.calculateDuration(calendarKey, startDate, endDate, periods);
    }
}
