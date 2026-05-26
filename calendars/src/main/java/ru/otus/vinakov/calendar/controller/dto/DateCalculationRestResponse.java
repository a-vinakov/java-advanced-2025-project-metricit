package ru.otus.vinakov.calendar.controller.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateCalculationRestResponse {

    private LocalDateTime date;
    private ZoneId timezone;

}
