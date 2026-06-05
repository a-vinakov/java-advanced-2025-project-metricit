package ru.otus.vinakov.analytic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfiguration {

    @Bean("gatewayRestClient")
    public RestClient restClient(@Value("${gateway.host}") String gatewayHost) {
        // Configure standard connect and read timeouts
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        requestFactory.setReadTimeout((int) Duration.ofSeconds(5).toMillis());

        return RestClient.builder()
                .baseUrl(gatewayHost)
                .requestFactory(requestFactory)
                .defaultHeader("Accept", "application/json")
                .build();
    }

}
