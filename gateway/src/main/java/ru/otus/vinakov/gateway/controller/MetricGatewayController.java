package ru.otus.vinakov.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.otus.vinakov.metric.domain.dto.NumberEntityMetricDTO;
import ru.otus.vinakov.metric.domain.dto.TimeEntityMetricDTO;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class MetricGatewayController {

    private final WebClient metricWebClient;

    @GetMapping("/rest/api/metric/entity/{entityKey}/time")
    public Flux<TimeEntityMetricDTO> getTimeMetrics(@PathVariable String entityKey) {
        return metricWebClient
                .get()
                //todo
                .uri("/rest/metric/entity/" + entityKey + "/time")
                .retrieve()
                .bodyToFlux(TimeEntityMetricDTO.class)
                .timeout(Duration.ofSeconds(10));
    }

    @GetMapping("/rest/api/metric/entity/{entityKey}/number")
    public Flux<NumberEntityMetricDTO> getNumberMetrics(@PathVariable String entityKey) {
        return metricWebClient
                .get()
                //todo
                .uri("/rest/metric/entity/" + entityKey + "/number")
                .retrieve()
                .bodyToFlux(NumberEntityMetricDTO.class)
                .timeout(Duration.ofSeconds(10));
    }

}
