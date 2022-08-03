package com.example.httpclient.http.apache;

import com.example.httpclient.http.dto.UpdateDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Client {

    private Logger log = LoggerFactory.getLogger(Client.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    public void request() {
        log.info("Client.request started");

        UpdateDto updateDto = new UpdateDto(1L, "success");

        StringEntity requestEntity;

        try {
            String s = objectMapper.writeValueAsString(updateDto);
            try {
                requestEntity = new StringEntity(s);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String url = "http://localhost:9090/update/" + updateDto.getId();

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

        // Apply timeout for the request
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(1 * 1000)
                .setConnectTimeout(1 * 1000)
                .setConnectionRequestTimeout(1 * 1000)
                .build();
        request.setConfig(requestConfig);

        request.setEntity(requestEntity);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        try {
            CloseableHttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);

            log.info("response statusCode={}, body={}", statusCode, responseBody);

            if (statusCode >= 200 && statusCode < 300) {
                log.info("Client.request success");
            } else {
                log.info("Client.request failed");
                throw new RuntimeException("Client.request failed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Client.request ended");
    }
}
