package ru.otus.vinakov.metric.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.vinakov.metric.domain.EntityMetric;
import ru.otus.vinakov.metric.domain.Metric;
import ru.otus.vinakov.metric.domain.NumberEntityMetric;
import ru.otus.vinakov.metric.domain.TimeEntityMetric;
import ru.otus.vinakov.metric.domain.dto.EntityMetricDTO;
import ru.otus.vinakov.metric.domain.dto.NumberEntityMetricDTO;
import ru.otus.vinakov.metric.domain.dto.TimeEntityMetricDTO;
import ru.otus.vinakov.metric.service.EntityMetricService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
public class EntityMetricController {

    private final EntityMetricService entityMetricService;
    private final Map<Metric.Category, Function<EntityMetric, EntityMetricDTO>> mappers = Map.of(
            Metric.Category.TIME, em -> TimeEntityMetricDTO.from((TimeEntityMetric) em, true),
            Metric.Category.NUMBER, em -> NumberEntityMetricDTO.from((NumberEntityMetric) em, true)
    );

    @GetMapping("/rest/metric/entity/{entityKey}/time")
    public List<EntityMetricDTO> getTimeEntityMetrics(@PathVariable String entityKey) {
        return entityMetricService.getAllTimeMetricsByEntityKey(entityKey).stream()
                .map(em -> mappers.get(em.getCategory()).apply(em))
                .toList();
    }

    @GetMapping("/rest/metric/entity/{entityKey}/number")
    public List<EntityMetricDTO> getNumberEntityMetrics(@PathVariable String entityKey) {
        return entityMetricService.getAllNumberMetricsByEntityKey(entityKey).stream()
                .map(em -> mappers.get(em.getCategory()).apply(em))
                .toList();
    }
}
