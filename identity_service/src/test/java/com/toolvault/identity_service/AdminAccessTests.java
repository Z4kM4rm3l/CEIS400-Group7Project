package com.toolvault.identity_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies that /admin/** endpoints require ADMIN role.
 * MockMvc + @WithMockUser avoids needing real JWTs for unit tests.
 */
@ActiveProfiles("test")
@SpringBootTest(classes = com.toolvault.identity_service.IdentityServiceApplication.class)
@AutoConfigureMockMvc
public class AdminAccessTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "manager@toolvault.local", roles = {"ADMIN"})
    void manager_can_access_admin() throws Exception {
        mvc.perform(get("/admin/health"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "employee@toolvault.local", roles = {"USER"})
    void employee_cannot_access_admin() throws Exception {
        mvc.perform(get("/admin/health"))
                .andExpect(status().isForbidden());
    }
}
