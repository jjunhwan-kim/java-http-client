package com.example.httpclient.http.java;

import com.example.httpclient.http.dto.UpdateDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Client {
    private Logger log = LoggerFactory.getLogger(com.example.httpclient.http.apache.Client.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    public void request() {
        log.info("Client.request started");

        UpdateDto updateDto = new UpdateDto(1L, "fail");

        String requestEntity;
        try {
            requestEntity = objectMapper.writeValueAsString(updateDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        String url = "http://localhost:9090/update/" + updateDto.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMillis(1000))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestEntity))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("statusCode={}, body={}", response.statusCode(), response.body());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Client.request ended");
    }
}
