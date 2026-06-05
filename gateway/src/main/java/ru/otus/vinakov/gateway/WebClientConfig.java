package ru.otus.vinakov.gateway;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean("analyticWebClient")
    public WebClient analyticWebClient(@Value("${service.analytic.url}") String url) {
        return createWebClient(url);
    }

    @Bean("metricWebClient")
    public WebClient metricWebClient(@Value("${service.metric.url}") String url) {
        return createWebClient(url);
    }

    private WebClient createWebClient(String url) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000) // 3 сек на установление TCP
                .responseTimeout(Duration.ofSeconds(10))            // 10 сек на получение первого байта ответа
                .keepAlive(true);                                   // Переиспользование соединений (connection pooling)

        return WebClient.builder()
                .baseUrl(url)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

}
