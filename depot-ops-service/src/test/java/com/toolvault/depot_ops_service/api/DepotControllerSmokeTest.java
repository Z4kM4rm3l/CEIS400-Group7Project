package com.toolvault.depot_ops_service.api;

import com.toolvault.depot_ops_service.DepotOpsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = DepotOpsApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DepotControllerSmokeTest {

    @Autowired
    MockMvc mvc;

    @Test
    void contextLoads() { }

    @Test
    void listAssets_emptyAtStartInTestProfile() throws Exception {
        mvc.perform(get("/depot/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void checkinCheckoutAndTransfer() throws Exception {
        mvc.perform(post("/depot/assets/checkin")
                        .contentType("application/json")
                        .content("""
            {"tag":"TEST-01","model":"Demo"}
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tag").value("TEST-01"))
                .andExpect(jsonPath("$.status").value("IN_DEPOT"));

        mvc.perform(post("/depot/assets/checkout")
                        .contentType("application/json")
                        .content("""
            {"tag":"TEST-01"}
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OUT_WITH_TECH"));

        mvc.perform(post("/depot/transfers/request")
                        .contentType("application/json")
                        .content("""
            {"assetTag":"TEST-01","destWarehouseCode":"WH-ATL"}
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetTag").value("TEST-01"))
                .andExpect(jsonPath("$.state").value("REQUESTED"));
    }
}
