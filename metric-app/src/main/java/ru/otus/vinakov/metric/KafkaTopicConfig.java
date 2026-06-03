package ru.otus.vinakov.metric;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    @Qualifier("${kafka.metric.topic}")
    public NewTopic metricEventTopic(@Value("${kafka.metric.topic}") String topic,
                                     @Value("${kafka.metric.partitions}") Integer partitions) {
        return TopicBuilder.name(topic)
                .partitions(partitions)
                .replicas(1) // Для локального single-node Kafka
                .config("retention.ms", "604800000")   // Хранить 7 дней
                .config("cleanup.policy", "delete")    // Автоматически удалять старые сообщения
                .build();
    }

}
