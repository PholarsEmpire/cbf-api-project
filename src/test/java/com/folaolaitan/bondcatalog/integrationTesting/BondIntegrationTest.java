// src/test/java/com/folaolaitan/bondcatalog/it/BondIntegrationTest.java
package com.folaolaitan.bondcatalog.integrationTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.folaolaitan.bondcatalog.entity.Bond;
import com.folaolaitan.bondcatalog.repository.BondRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Integration tests for Bond API
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BondIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired BondRepository repo;

    @BeforeEach
    void seed() {
        repo.deleteAll();

        repo.save(new Bond(null, "Corp A 2028", "Corp A",
                new BigDecimal("1000"), new BigDecimal("5.00"), "AA",
                LocalDate.of(2023,1,1), LocalDate.of(2028,1,1), "USD"));

        repo.save(new Bond(null, "Gov B 2030", "Gov B",
                new BigDecimal("1000"), new BigDecimal("3.00"), "AAA",
                LocalDate.of(2020,1,1), LocalDate.of(2030,1,1), "USD"));
    }

    // Test retrieving all bonds
    @Test
    void getAll_returnsTwo() throws Exception {
        mvc.perform(get("/api/bonds"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(2)));
    }

    // Test creating a new bond and then retrieving it
    @Test
    void post_creates_then_retrieves() throws Exception {
        Bond incoming = new Bond(null, "Corp C 2031", "Corp C",
                new BigDecimal("1000"), new BigDecimal("4.50"), "A",
                LocalDate.of(2024,1,1), LocalDate.of(2031,1,1), "USD");

        String body = om.writeValueAsString(incoming);

        String response = mvc.perform(post("/api/bonds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andReturn().getResponse().getContentAsString();

        Bond saved = om.readValue(response, Bond.class);
        assertThat(repo.findById(saved.getId())).isPresent();
    }

    // Tests filtering bonds by rating
    @Test
    void filter_rating_AAA_returnsGovB() throws Exception {
        mvc.perform(get("/api/bonds/rating/AAA"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].issuer", is("Gov B")));
    }

    //Tests filtering bonds by issuer
    @Test
    void filter_issuer_CorpA_returnsCorpA() throws Exception {
        mvc.perform(get("/api/bonds/issuer/Corp A"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].name", is("Corp A 2028")));
    }

    // Tests the bond summary endpoint
    @Test
    void summary_hasTotals() throws Exception {
        mvc.perform(get("/api/bonds/summary"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.totalBonds", is(2)));
    }

    // Tests retrieving a bond by ID
    @Test
    void getById_404_returnsHandledError() throws Exception {
        mvc.perform(get("/api/bonds/99999"))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.error", is("Not Found")));
    }


    // Tests filtering bonds by maturity date
    @Test
    void maturityDateAfter_IT() throws Exception {
        mvc.perform(get("/api/bonds/maturity-date/2029-12-31"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[*].name", hasItem("Gov B 2030")));
    }

    // Tests filtering bonds by minimum coupon rate
    @Test
    void couponRateAtLeast_IT() throws Exception {
        mvc.perform(get("/api/bonds/coupon-rate/3"))
        .andExpect(status().isOk());
    }

    // Tests filtering bonds by maximum coupon rate
    @Test
    void couponRateAtMost_IT() throws Exception {
        mvc.perform(get("/api/bonds/coupon-rate/6"))
        .andExpect(status().isOk());
    }

    // Tests filtering bonds by maximum coupon rate with no results
    @Test
    void couponRateAtMost_noResults_IT() throws Exception {
        mvc.perform(get("/api/bonds/coupon-rate/6"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
    }


    // Tests filtering bonds by coupon rate range
    @Test
    void couponRateBetween_IT() throws Exception {
        mvc.perform(get("/api/bonds/coupon-rate/3/6"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", not(empty())));
    }

    // Tests filtering bonds by face value range
    @Test
    void faceValueBetween_IT() throws Exception {
        mvc.perform(get("/api/bonds/face-value-between?min-value=500&max-value=1500"))
        .andExpect(status().isOk());
    }

    // Tests filtering bonds by status
    @Test
    void status_empty_OK_withHeader_IT() throws Exception {
        mvc.perform(get("/api/bonds/status?status=Defaulted"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-Message", containsString("No bonds")))
        .andExpect(jsonPath("$", hasSize(0)));
    }

    // Tests the full create, update, delete flow for a bond
    @Test
    void create_update_delete_flow_IT() throws Exception {
        // 1) CREATE
        Bond incoming = new Bond(null, "Flow 2032", "FlowCorp",
                new BigDecimal("1000"), new BigDecimal("4.20"), "A",
                LocalDate.of(2024, 6, 1), LocalDate.of(2032, 6, 1), "USD");

        String createdJson = mvc.perform(post("/api/bonds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(incoming)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn().getResponse().getContentAsString();

        Bond saved = om.readValue(createdJson, Bond.class);

        // 2) UPDATE
        Bond patch = new Bond(null, "Flow 2032 Updated", "FlowCorp",
                new BigDecimal("2000"), new BigDecimal("4.50"), "AA",
                incoming.getIssueDate(), incoming.getMaturityDate(), "USD");

        mvc.perform(put("/api/bonds/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Flow 2032 Updated")))
                .andExpect(jsonPath("$.faceValue", is(2000)));

        // 3) DELETE
        mvc.perform(delete("/api/bonds/" + saved.getId()))
                .andExpect(status().isOk());

        assertThat(repo.findById(saved.getId())).isEmpty();
    }

    // Tests searching bonds by issuer substring
    @Test
    void issuer_search_IT() throws Exception {
        mvc.perform(get("/api/bonds/issuer/Gov"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].issuer", hasItem("Gov B")));
    }

    // Tests filtering bonds by status for invalid statuses
    @Test
    void status_invalid_400_IT() throws Exception {
        mvc.perform(get("/api/bonds/status?status=Weird"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")));
    }



}
