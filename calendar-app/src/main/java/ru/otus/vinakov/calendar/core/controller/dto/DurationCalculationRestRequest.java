package ru.otus.vinakov.calendar.core.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DurationCalculationRestRequest implements WithPeriodsRestRequest {

    private String calendarKey;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startDate;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime endDate;
    @Pattern(regexp = "^GMT[+-]((0?[0-9]|1[0-1]):([0-5][0-9])|12:00)$",
            message = "Invalid timezone format! Valid format: GMT+/-XX:XX")
    private String timezone;
    private List<@Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)-([01]\\d|2[0-3]):([0-5]\\d)$",
            message = "Invalid interval format! Valid format: hh:mm-hh:mm") String> periods;

}
