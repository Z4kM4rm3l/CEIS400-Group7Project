package com.toolvault.reporting_service.client;

import com.toolvault.reporting_service.dto.DepotActivityDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class DepotClient {
    private final RestTemplate rest = new RestTemplate();

    @Value("${toolvault.depot.base-url:http://localhost:8081}")
    private String baseUrl;

    public List<DepotActivityDto> fetchActivities(String bearerToken) {
        String url = baseUrl + "/api/depot/activities"; // expected Depot endpoint
        HttpHeaders headers = new HttpHeaders();
        if (bearerToken != null && !bearerToken.isBlank()) {
            headers.set("Authorization", "Bearer " + bearerToken);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<DepotActivityDto[]> res = rest.exchange(
                url, HttpMethod.GET, entity, DepotActivityDto[].class);
        DepotActivityDto[] body = res.getBody();
        return Arrays.asList(body == null ? new DepotActivityDto[]{} : body);
    }
}
