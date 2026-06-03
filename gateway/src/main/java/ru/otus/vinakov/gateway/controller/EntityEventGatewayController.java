package ru.otus.vinakov.gateway.controller;

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
public class EntityEventGatewayController {

    private final KafkaMessageService kafkaMessageService;

    @PostMapping("/rest/api/metric/event")
    public Mono<ResponseEntity<EntityEventResponseDTO>> sendMessage(@Valid @RequestBody EntityEventDTO request) {
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
