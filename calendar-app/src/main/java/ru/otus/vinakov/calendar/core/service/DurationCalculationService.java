package ru.otus.vinakov.calendar.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.vinakov.calendar.api.WorkPeriod;
import ru.otus.vinakov.calendar.core.Constants;
import ru.otus.vinakov.calendar.core.model.DayType;
import ru.otus.vinakov.calendar.core.model.ProductionCalendar;
import ru.otus.vinakov.calendar.core.model.ProductionCalendarDay;
import ru.otus.vinakov.calendar.core.model.DatePoint;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class DurationCalculationService {

    private final ProductionCalendarService productionCalendarService;
    private final Integer maxDurationRange;

    public DurationCalculationService(ProductionCalendarService productionCalendarService,
                                      @Value("${calendar.limits.max-duration-range}") Integer maxDurationRange) {
        this.productionCalendarService = productionCalendarService;
        this.maxDurationRange = maxDurationRange != null ? maxDurationRange : Constants.MAX_DURATION_RANGE;
    }

    public Long calculate(String calendarKey, ZonedDateTime startDate, ZonedDateTime endDate, List<WorkPeriod> periods) {
        ProductionCalendar calendar = productionCalendarService.getCalendar(calendarKey)
                //todo RTE
                .orElseThrow(() -> new RuntimeException(String.format("Calendar with key %s not found", calendarKey)));
        ZonedDateTime dateIndex = startDate.truncatedTo(ChronoUnit.DAYS);
        long startDateInSeconds = startDate.getLong(ChronoField.INSTANT_SECONDS);
        long endDateInSeconds = endDate.getLong(ChronoField.INSTANT_SECONDS);
        List<DatePoint> datePoints = DatePoint.getDayDatePoints(periods);
        long result = 0;
        int delta;
        while (dateIndex.getLong(ChronoField.INSTANT_SECONDS) < endDateInSeconds) {
            ProductionCalendarDay day = calendar.getDay(dateIndex.toLocalDate());
            if (day != null && day.getType() == DayType.WEEKEND) {
                dateIndex = dateIndex.plusDays(1);
            } else {
                DatePoint prevDatePoint = DatePoint.START_OF_DAY;
                boolean startDateSteppedOver = false;
                for (DatePoint datePoint : datePoints) {
                    if (dateIndex.getLong(ChronoField.INSTANT_SECONDS) + (datePoint.getOffset() - prevDatePoint.getOffset()) >= startDateInSeconds) {
                        if (!startDateSteppedOver) {
                            startDateSteppedOver = true;
                        }
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
                                        int emptyInterval = (int) (startDateInSeconds - dateIndex.getLong(ChronoField.INSTANT_SECONDS));
                                        delta -= emptyInterval;
                                        dateIndex = dateIndex.plusSeconds(emptyInterval);
                                    }
                                    if (dateIndex.getLong(ChronoField.INSTANT_SECONDS) + delta >= endDateInSeconds) {
                                        result += endDateInSeconds - dateIndex.getLong(ChronoField.INSTANT_SECONDS);
                                        return result;
                                    } else {
                                        result += delta;
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
                    //если мы уже перешагнули в рассчетах конечную дату - нет смысла идти дальше, или потом в дельте получим отрицательные значения
                    if (dateIndex.getLong(ChronoField.INSTANT_SECONDS) >= endDateInSeconds) {
                        break;
                    } else {
                        prevDatePoint = datePoint;
                    }
                }
            }
            if (result > maxDurationRange) {
                throw new RuntimeException(String.format("Exceed max duration range! Max range: %d. Maybe, it is infinitive loop", Constants.MAX_DURATION_RANGE));
            }
        }
        return result;
    }

}
