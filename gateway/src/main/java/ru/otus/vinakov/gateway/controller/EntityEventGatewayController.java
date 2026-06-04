package ru.otus.vinakov.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.vinakov.gateway.dto.EntityEventDTO;
import ru.otus.vinakov.gateway.dto.EntityEventResponseDTO;
import ru.otus.vinakov.gateway.service.KafkaMessageService;

import java.time.Instant;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Register entity events")
public class EntityEventGatewayController {

    private final KafkaMessageService kafkaMessageService;

    @Operation(summary = "Register new entity event")
    @PostMapping("/rest/api/metric/event")
    @ApiResponses({@ApiResponse(description = "Returns status message and date, when entity event was registered", responseCode = "202")})
    public Mono<ResponseEntity<EntityEventResponseDTO>> sendMessage(@Valid @RequestBody
                                                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                                            description = "Entity event with entity key, event date and entity properties",
                                                                            required = true,
                                                                            content = @Content(
                                                                                    mediaType = "application/json",
                                                                                    schema = @Schema(implementation = EntityEventResponseDTO.class),
                                                                                    examples = @ExampleObject(
                                                                                            name = "Example of Jira issue fields",
                                                                                            value = "{\"schemaKey\":\"TEST\",\"entityKey\":\"TEST-1\",\"eventDate\":\"03.06.2026 17:47:40\",\"attributes\":{\"priority\":\"Medium\",\"status\":\"To Do\"}}"
                                                                                    )
                                                                            )
                                                                    )
                                                                    EntityEventDTO request) {
        return kafkaMessageService.send(request)
                .thenReturn(ResponseEntity.accepted()
                        .body(new EntityEventResponseDTO("Entity event successfully accepted", LocalDateTime.now())))
                .onErrorResume(ex -> {
                    log.error("Запрос не обработан", ex);
                    return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                            .body(new EntityEventResponseDTO("Entity event was not recieved. Reason: " + ex.getMessage(), LocalDateTime.now())));
                });
    }

}
