package ru.otus.vinakov.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Gateway to get entities metrics by entity key")
public class MetricGatewayController {

    private final WebClient metricWebClient;

    @Operation(summary = "Get time metrics by entity key")
    @GetMapping("/rest/api/metric/entity/{entityKey}/time")
    @ApiResponses({@ApiResponse(description = "Returns list of time metrics, attached to entity", responseCode = "200")})
    public Flux<TimeEntityMetricDTO> getTimeMetrics(@PathVariable String entityKey) {
        return metricWebClient
                .get()
                //todo
                .uri("/rest/metric/entity/" + entityKey + "/time")
                .retrieve()
                .bodyToFlux(TimeEntityMetricDTO.class)
                .timeout(Duration.ofSeconds(10));
    }

    @Operation(summary = "Get number metrics by entity key")
    @GetMapping("/rest/api/metric/entity/{entityKey}/number")
    @ApiResponses({@ApiResponse(description = "Returns list of number metrics, attached to entity", responseCode = "200")})
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
