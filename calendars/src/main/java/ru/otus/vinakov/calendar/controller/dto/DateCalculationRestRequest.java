package ru.otus.vinakov.calendar.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class DateCalculationRestRequest implements WithPeriodsRestRequest {

    private String calendarKey;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startDate;
    private Long duration;
    @Pattern(regexp = "^GMT[+-]((0?[0-9]|1[0-1]):([0-5][0-9])|12:00)$",
            message = "Invalid timezone format! Valid format: GMT+/-XX:XX")
    private String timezone;
    private List<@Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)-([01]\\d|2[0-3]):([0-5]\\d)$",
            message = "Invalid interval format! Valid format: hh:mm-hh:mm") String> periods;

}
