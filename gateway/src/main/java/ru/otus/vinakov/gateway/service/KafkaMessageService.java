package ru.otus.vinakov.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.vinakov.gateway.dto.EntityEventDTO;
import ru.otus.vinakov.metric.domain.event.KafkaEntityEvent;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class KafkaMessageService {

    private final KafkaTemplate<String, KafkaEntityEvent> kafkaTemplate;
    private final String topic;

    public KafkaMessageService(KafkaTemplate<String, KafkaEntityEvent> kafkaTemplate, @Value("${spring.kafka.topic.metric}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public Mono<SendResult<String, KafkaEntityEvent>> send(EntityEventDTO event) {
        return Mono.fromCallable(() -> {
                    return kafkaTemplate.send(topic, event.getEntityKey(),
                                    new KafkaEntityEvent(event.getEntityKey(), event.getSchemaKey(), event.getEventDate(),
                                            event.getTimezone(), event.getAttributes()))
                            .get(5, TimeUnit.SECONDS);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(result ->
                        log.info("EntityEvent sent to Kafka. Offset: {}, Partition: {}",
                                result.getRecordMetadata().offset(),
                                result.getRecordMetadata().partition()))

                .doOnError(error ->
                        log.error("EntityEvent sending to Kafka failed. Key: {}", getEventId(event), error))

                .timeout(Duration.ofSeconds(5))
                .onErrorMap(java.util.concurrent.TimeoutException.class,
                        ex -> new RuntimeException("Kafka send timeout after 5s", ex));
    }

    private String getEventId(EntityEventDTO event) {
        return String.format("%s:%s:%s", event.getSchemaKey(), event.getEntityKey(), event.getEventDate());
    }

}
