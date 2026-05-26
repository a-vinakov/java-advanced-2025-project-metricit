package ru.otus.vinakov.calendar.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.vinakov.calendar.Constants;
import ru.otus.vinakov.calendar.model.DayType;
import ru.otus.vinakov.calendar.model.ProductionCalendar;
import ru.otus.vinakov.calendar.model.ProductionCalendarDay;
import ru.otus.vinakov.calendar.model.WorkPeriod;
import ru.otus.vinakov.calendar.model.DatePoint;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class DateCalculationService {

    private final ProductionCalendarService productionCalendarService;

    public DateCalculationService(ProductionCalendarService productionCalendarService) {
        this.productionCalendarService = productionCalendarService;
    }

    public ZonedDateTime calculate(String calendarKey, ZonedDateTime startDate, Long duration, List<WorkPeriod> periods) {
        ProductionCalendar calendar = productionCalendarService.getCalendar(calendarKey)
                //todo RTE
                .orElseThrow(() -> new RuntimeException(String.format("Calendar with key %s not found", calendarKey)));
        ZonedDateTime dateIndex = startDate.truncatedTo(ChronoUnit.DAYS);
        long startDateInSeconds = startDate.getLong(ChronoField.INSTANT_SECONDS);
        long remain = duration;
        int startYear = dateIndex.getYear();
        List<DatePoint> datePoints = DatePoint.getDayDatePoints(periods);
        while (remain > 0) {
            ProductionCalendarDay day = calendar.getDay(dateIndex.toLocalDate());
            if (day != null && day.getType() == DayType.WEEKEND) {
                dateIndex = dateIndex.plusDays(1);
            } else {
                DatePoint prevDatePoint = DatePoint.START_OF_DAY;
                long delta;
                for (DatePoint datePoint : datePoints) {
                    //если мы уже перешагнули дату старта или достигли ее в текущем периоде
                    //if (jCalendar.getTimeAsSeconds() + (datePoint.getOffset() - prevDatePoint.getOffset()) >= startDateInSeconds) {
                    if (dateIndex.getLong(ChronoField.INSTANT_SECONDS) + (datePoint.getOffset() - prevDatePoint.getOffset()) >= startDateInSeconds) {
                        switch (datePoint.getType()) {
                            case START_OF_DAY:
                            case END_OF_DAY:
                            case START_OF_WP:
                                dateIndex = dateIndex.plusSeconds(datePoint.getOffset() - prevDatePoint.getOffset());
                                break;
                            case END_OF_WP:
                                if (prevDatePoint.getType() == DatePoint.Type.START_OF_WP) {
                                    delta = datePoint.getOffset() - prevDatePoint.getOffset();
                                    if (startDateInSeconds > dateIndex.getLong(ChronoField.INSTANT_SECONDS)
                                            && startDateInSeconds - dateIndex.getLong(ChronoField.INSTANT_SECONDS) <= delta) {
                                        //тот случай, когда стартовая дата находится в текущем периоде, но мы ее еще не достигли.
                                        //в этом случае нужно откинуть отрезок от текущего индекса до стартовой даты
                                        delta -= startDateInSeconds - dateIndex.getLong(ChronoField.INSTANT_SECONDS);
                                        dateIndex = dateIndex.plusSeconds(startDateInSeconds - dateIndex.getLong(ChronoField.INSTANT_SECONDS));
                                    }
                                    if (delta >= remain) {
                                        dateIndex = dateIndex.plusSeconds(remain);
                                        return dateIndex;
                                    } else {
                                        remain -= delta;
                                        dateIndex = dateIndex.plusSeconds(delta);
                                    }
                                } else {
                                    throw new RuntimeException("Invalid timeline! Actual date point: END_OF_PERIOD, previous date point: " + prevDatePoint.getType());
                                }
                                break;
                        }
                    } else {
                        dateIndex = dateIndex.plusSeconds(datePoint.getOffset() - prevDatePoint.getOffset());
                    }
                    prevDatePoint = datePoint;
                }
            }
            if (dateIndex.getYear() - startYear > Constants.MAX_YEAR_RANGE) {
                throw new RuntimeException(String.format("Exceed max year range! Max range: %d. Maybe, it is infinitive loop", Constants.MAX_YEAR_RANGE));
            }
        }
        return null;
    }
}
