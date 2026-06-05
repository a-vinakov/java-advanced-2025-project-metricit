package ru.otus.vinakov.jira.plugin.listener;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class JiraHttpClient {

    private final String defaultUrl;
    private final Map<String, String> defaultHeaders;
    private final HttpClient httpClient;

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    public JiraHttpClient(String defaultUrl, Map<String, String> defaultHeaders) {
        this.defaultUrl = defaultUrl;
        this.defaultHeaders = defaultHeaders;
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public <T, R> R post(String url, T body, Map<String, String> headers, TypeReference<R> typeReference) throws IOException, InterruptedException {
//        String url = buildUrl(endpoint);
        String fullUrl = defaultUrl + url;
        String jsonBody = JSON_MAPPER.writeValueAsString(body);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(30));
        defaultHeaders.forEach(requestBuilder::header);
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());
        return parseResponse(response, typeReference);
    }

    //todo params to url
    public <R> R get(String url, Map<String, String> headers, TypeReference<R> typeReference) throws IOException, InterruptedException {
        String fullUrl = defaultUrl + url;
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .timeout(Duration.ofSeconds(30));
        defaultHeaders.forEach(requestBuilder::header);
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }
        HttpRequest request = requestBuilder
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return parseResponse(response, typeReference);
    }

    private <R> R parseResponse(HttpResponse<String> response, TypeReference<R> typeReference) {
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            try {
//                if (responseType == String.class) {
//                    return responseType.cast(response.body());
//                }
                return JSON_MAPPER.readValue(response.body(), typeReference);
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse response", e);
            }
        } else {
            throw new RuntimeException("HTTP " + response.statusCode() + ": " + response.body());
        }
    }


}
