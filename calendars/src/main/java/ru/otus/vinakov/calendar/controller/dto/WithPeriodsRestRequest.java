package ru.otus.vinakov.calendar.controller.dto;

import ru.otus.vinakov.calendar.model.WorkPeriod;

import java.util.ArrayList;
import java.util.List;

public interface WithPeriodsRestRequest {

    List<String> getPeriods();

    default List<WorkPeriod> getWorkPeriods() {
        List<WorkPeriod> workPeriods = new ArrayList<>();
        for (String rawPeriod : getPeriods()) {
            String[] rawPeriods = rawPeriod.trim().split("-");
            workPeriods.add(WorkPeriod.fromStrings(rawPeriods[0], rawPeriods[1]));
        }
        return workPeriods;
    }

}
