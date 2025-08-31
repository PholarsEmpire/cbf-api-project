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

    @Test
    void getAll_returnsTwo() throws Exception {
        mvc.perform(get("/api/bonds"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(2)));
    }

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

    @Test
    void filter_rating_AAA_returnsGovB() throws Exception {
        mvc.perform(get("/api/bonds/rating/AAA"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].issuer", is("Gov B")));
    }

    @Test
    void summary_hasTotals() throws Exception {
        mvc.perform(get("/api/bonds/summary"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.totalBonds", is(2)));
    }

    @Test
    void getById_404_returnsHandledError() throws Exception {
        mvc.perform(get("/api/bonds/99999"))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.error", is("Not Found")));
    }

    @Test
    void maturityDateAfter_IT() throws Exception {
        mvc.perform(get("/api/bonds/maturity-date/2029-12-31"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[*].name", hasItem("Gov B 2030")));
    }

    @Test
    void couponRateAtLeast_IT() throws Exception {
        mvc.perform(get("/api/bonds/coupon-rate/3"))
        .andExpect(status().isOk());
    }

    @Test
    void couponRateAtMost_IT() throws Exception {
        mvc.perform(get("/api/bonds/coupon-rate/6"))
        .andExpect(status().isOk());
    }

    @Test
    void couponRateAtMost_noResults_IT() throws Exception {
        mvc.perform(get("/api/bonds/coupon-rate/6"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    void couponRateBetween_IT() throws Exception {
        mvc.perform(get("/api/bonds/coupon-rate/3/6"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void faceValueBetween_IT() throws Exception {
        mvc.perform(get("/api/bonds/face-value-between?min-value=500&max-value=1500"))
        .andExpect(status().isOk());
    }

    @Test
    void status_empty_OK_withHeader_IT() throws Exception {
        mvc.perform(get("/api/bonds/status?status=Defaulted"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-Message", containsString("No bonds")))
        .andExpect(jsonPath("$", hasSize(0)));
    }


}
