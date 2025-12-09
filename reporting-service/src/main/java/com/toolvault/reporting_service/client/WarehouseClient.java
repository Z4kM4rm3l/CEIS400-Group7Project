package com.toolvault.reporting_service.client;

import com.toolvault.reporting_service.dto.WarehouseStockDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class WarehouseClient {
    private final RestTemplate rest = new RestTemplate();

    @Value("${toolvault.warehouse.base-url:http://localhost:8082}")
    private String baseUrl;

    public List<WarehouseStockDto> fetchStock(String bearerToken) {
        String url = baseUrl + "/api/warehouse/items"; // expected Warehouse endpoint
        HttpHeaders headers = new HttpHeaders();
        if (bearerToken != null && !bearerToken.isBlank()) {
            headers.set("Authorization", "Bearer " + bearerToken);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<WarehouseStockDto[]> res = rest.exchange(
                url, HttpMethod.GET, entity, WarehouseStockDto[].class);
        WarehouseStockDto[] body = res.getBody();
        return Arrays.asList(body == null ? new WarehouseStockDto[]{} : body);
    }
}
