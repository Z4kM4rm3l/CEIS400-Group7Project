package com.toolvault.depot_ops_service.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WarehouseClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public WarehouseClient(RestTemplate restTemplate,
                           @Value("${warehouse.base-url:http://localhost:8082}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public void restock(String sku, int qty, String name) {
        String url = baseUrl + "/warehouse/restock";
        String body = String.format("{\"sku\":\"%s\",\"quantity\":%d,\"name\":\"%s\"}", sku, qty, name == null ? "" : name);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
    }

    public void allocate(String sku, int qty) {
        String url = baseUrl + "/warehouse/allocate";
        String body = String.format("{\"sku\":\"%s\",\"quantity\":%d}", sku, qty);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
    }
}
