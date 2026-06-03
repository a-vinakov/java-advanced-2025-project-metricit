package ru.otus.vinakov.calendar.core.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.vinakov.calendar.core.controller.dto.DateCalculationRestRequest;
import ru.otus.vinakov.calendar.core.controller.dto.DateCalculationRestResponse;
import ru.otus.vinakov.calendar.core.controller.dto.DurationCalculationRestRequest;
import ru.otus.vinakov.calendar.core.controller.dto.DurationCalculationRestResponse;
import ru.otus.vinakov.calendar.core.service.DateCalculationService;
import ru.otus.vinakov.calendar.core.service.DurationCalculationService;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CalendarCalculationController {

    private final DateCalculationService dateCalculationService;
    private final DurationCalculationService durationCalculationService;

    @PostMapping("/rest/calendar/date")
    public ResponseEntity<DateCalculationRestResponse> calculateDate(@Valid @RequestBody DateCalculationRestRequest request) {
        ZonedDateTime dateTime = dateCalculationService.calculate(request.getCalendarKey(),
                ZonedDateTime.of(request.getStartDate(), ZoneId.of(request.getTimezone())),
                request.getDuration(),
                request.getWorkPeriods());
        return ResponseEntity.ok(new DateCalculationRestResponse(dateTime.toLocalDateTime(), ZoneId.of(request.getTimezone())));
    }

    @PostMapping("/rest/calendar/duration")
    public ResponseEntity<DurationCalculationRestResponse> calculateDuration(@Valid @RequestBody DurationCalculationRestRequest request) {
        Long duration = durationCalculationService.calculate(request.getCalendarKey(),
                ZonedDateTime.of(request.getStartDate(), ZoneId.of(request.getTimezone())),
                ZonedDateTime.of(request.getEndDate(), ZoneId.of(request.getTimezone())),
                request.getWorkPeriods());
        return ResponseEntity.ok(new DurationCalculationRestResponse(duration));
    }

}
