package ru.otus.vinakov.metric;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EntityScan(basePackages = "ru.otus.vinakov.metric.domain")
@EnableJpaRepositories(basePackages = "ru.otus.vinakov.metric.repository")
public class MetricApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetricApplication.class, args);
    }

    @Bean
    @Qualifier("calendar")
    public Channel calendarChanner(@Value("${grpc.calendar.host}") String host,
                                   @Value("${grpc.calendar.port}") Integer port) {
        return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    }

}
