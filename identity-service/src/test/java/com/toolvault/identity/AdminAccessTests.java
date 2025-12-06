package com.toolvault.identity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminAccessTests {

  @LocalServerPort
  int port;

  @Autowired
  private TestRestTemplate rest;

  @Test
  void manager_can_access_admin_employee_cannot() {
    // login manager
    String managerToken = login("manager@toolvault.local", "Mgr123!");
    assertNotNull(managerToken);

    // manager hits /admin/ping
    HttpHeaders mh = new HttpHeaders();
    mh.setBearerAuth(managerToken);
    ResponseEntity<Map> adminOk = rest.exchange(url("/admin/ping"), HttpMethod.GET, new HttpEntity<>(mh), Map.class);
    assertEquals(HttpStatus.OK, adminOk.getStatusCode());
    assertEquals("pong", adminOk.getBody().get("status"));

    // login employee
    String empToken = login("employee@toolvault.local", "Emp123!");
    assertNotNull(empToken);

    // employee hits /admin/ping -> 403
    HttpHeaders eh = new HttpHeaders();
    eh.setBearerAuth(empToken);
    ResponseEntity<String> adminForbidden = rest.exchange(url("/admin/ping"), HttpMethod.GET, new HttpEntity<>(eh), String.class);
    assertEquals(HttpStatus.FORBIDDEN, adminForbidden.getStatusCode());
  }

  private String login(String email, String password) {
    Map<String, String> body = Map.of("email", email, "password", password);
    ResponseEntity<Map> resp = rest.postForEntity(url("/auth/login"), body, Map.class);
    if (resp.getStatusCode() != HttpStatus.OK) return null;
    return (String) resp.getBody().get("accessToken");
  }

  private String url(String path) { return "http://localhost:" + port + path; }
}
