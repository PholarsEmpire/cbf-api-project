package com.folaolaitan.bondcatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.folaolaitan.bondcatalog.customexceptions.BadRequestException;
import com.folaolaitan.bondcatalog.customexceptions.GlobalExceptionHandler;
import com.folaolaitan.bondcatalog.customexceptions.ResourceNotFoundException;
import com.folaolaitan.bondcatalog.entity.Bond;
import com.folaolaitan.bondcatalog.service.BondService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.AdditionalMatchers.eq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BondController.class)
@Import(GlobalExceptionHandler.class)
class BondControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockBean BondService service;

    private Bond b() {
        return new Bond(1L, "US Treasury 10Y", "US Government",
                new BigDecimal("1000"), new BigDecimal("3.50"), "AAA",
                LocalDate.of(2020,1,1), LocalDate.of(2030,1,1), "USD");
    }

    // ---- CRUD ----

    @Test
    void listAll_ok() throws Exception {
        Mockito.when(service.getAllBonds()).thenReturn(List.of(b()));
        mvc.perform(get("/api/bonds"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].issuer", is("US Government")));
    }

    @Test
    void getById_ok() throws Exception {
        Mockito.when(service.getBondById(1L)).thenReturn(b());
        mvc.perform(get("/api/bonds/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is("US Treasury 10Y")));
    }

    @Test
    void getById_404() throws Exception {
        Mockito.when(service.getBondById(99L))
               .thenThrow(new ResourceNotFoundException("Bond with id 99 not found."));
        mvc.perform(get("/api/bonds/99"))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.error", is("Not Found")));
    }

    @Test
    void create_ok() throws Exception {
        Bond incoming = new Bond(null, "Corp A 2028", "Corp A",
                new BigDecimal("1000"), new BigDecimal("5.00"), "AA",
                LocalDate.of(2023,1,1), LocalDate.of(2028,1,1), "USD");
        Mockito.when(service.createBond(any(Bond.class)))
               .thenReturn(new Bond(10L, incoming.getName(), incoming.getIssuer(),
                       incoming.getFaceValue(), incoming.getCouponRate(), incoming.getRating(),
                       incoming.getIssueDate(), incoming.getMaturityDate(), "USD"));
        mvc.perform(post("/api/bonds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(incoming)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id", is(10)));
    }

    @Test
    void create_badRequest_400() throws Exception {
        Bond incoming = b(); // has id, will trigger BadRequest in service
        Mockito.when(service.createBond(any(Bond.class)))
               .thenThrow(new BadRequestException("Bond ID must be null"));
        mvc.perform(post("/api/bonds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(incoming)))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.error", is("Bad Request")));
    }

    @Test
    void update_ok() throws Exception {
        Bond patch = new Bond(null, "Updated", "IssuerX",
                new BigDecimal("2000"), new BigDecimal("4.00"), "AA",
                LocalDate.of(2021,1,1), LocalDate.of(2031,1,1), "USD");
        Mockito.when(service.updateBond(eq(1L), any(Bond.class))).thenReturn(b());
        mvc.perform(put("/api/bonds/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(patch)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name", is("US Treasury 10Y")));
    }

    @Test
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/bonds/1"))
           .andExpect(status().isOk());
        Mockito.verify(service).deleteBond(1L);
    }

    // ---- FILTERS ----

    @Test
    void issuer_ok() throws Exception {
        Mockito.when(service.findBondsByIssuer("apple")).thenReturn(List.of(b()));
        mvc.perform(get("/api/bonds/issuer/apple"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].name", notNullValue()));
    }

    @Test
    void rating_ok() throws Exception {
        Mockito.when(service.findBondsByRating("AAA")).thenReturn(List.of(b()));
        mvc.perform(get("/api/bonds/rating/AAA"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].rating", is("AAA")));
    }

    @Test
    void couponAtLeast_ok() throws Exception {
        Mockito.when(service.findBondsByCouponRateGreaterThanEqual(new BigDecimal("3")))
               .thenReturn(List.of(b()));
        mvc.perform(get("/api/bonds/coupon-rate/3"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].couponRate", is(3.50)));
    }

    @Test
    void couponAtLeast_invalid_400() throws Exception {
        mvc.perform(get("/api/bonds/coupon-rate/0"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void couponBetween_ok() throws Exception {
        Mockito.when(service.findBondsByCouponRateBetween(3.0, 6.0))
               .thenReturn(List.of(b()));
        mvc.perform(get("/api/bonds/coupon-rate/3/6"))
           .andExpect(status().isOk());
    }

    @Test
    void couponBetween_invalid_400() throws Exception {
        mvc.perform(get("/api/bonds/coupon-rate/6/3"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void maturingBetween_ok() throws Exception {
        Mockito.when(service.findBondsByMaturityDateBetween(any(), any()))
               .thenReturn(List.of(b()));
        mvc.perform(get("/api/bonds/maturing-between/2029-01-01/2031-01-01"))
           .andExpect(status().isOk());
    }

    @Test
    void maturingBetween_badDate_400() throws Exception {
        mvc.perform(get("/api/bonds/maturing-between/2029-01-01/not-a-date"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void maturityAfter_ok() throws Exception {
        Mockito.when(service.findBondsByMaturityDateAfter(any()))
               .thenReturn(List.of(b()));
        mvc.perform(get("/api/bonds/maturity-date/2030-12-31"))
           .andExpect(status().isOk());
    }

    @Test
    void maturityAfter_badDate_400() throws Exception {
        mvc.perform(get("/api/bonds/maturity-date/bad"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void issuedBetween_ok() throws Exception {
        Mockito.when(service.findBondsByIssueDateBetween(any(), any()))
               .thenReturn(List.of(b()));
        mvc.perform(get("/api/bonds/issued-between?start-date=2020-01-01&end-date=2024-12-31"))
           .andExpect(status().isOk());
    }

    @Test
    void issuedBetween_badDate_400() throws Exception {
        mvc.perform(get("/api/bonds/issued-between?start-date=2020-01-01&end-date=bad"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void issueAfter_ok() throws Exception {
        Mockito.when(service.findBondsByIssueDateAfter(any()))
               .thenReturn(List.of(b()));
        mvc.perform(get("/api/bonds/issue-date/2020-01-01"))
           .andExpect(status().isOk());
    }

    @Test
    void issueAfter_badDate_400() throws Exception {
        mvc.perform(get("/api/bonds/issue-date/bad"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void faceValueGte_ok() throws Exception {
        Mockito.when(service.findBondsByFaceValueGreaterThanEqual(new BigDecimal("1000")))
               .thenReturn(List.of(b()));
        mvc.perform(get("/api/bonds/face-value/1000"))
           .andExpect(status().isOk());
    }

    @Test
    void faceValueGte_invalid_400() throws Exception {
        mvc.perform(get("/api/bonds/face-value/0"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void faceValueBetween_ok() throws Exception {
        Mockito.when(service.findBondsByFaceValueBetween(new BigDecimal("500"), new BigDecimal("1500")))
               .thenReturn(List.of(b()));
        mvc.perform(get("/api/bonds/face-value-between?min-value=500&max-value=1500"))
           .andExpect(status().isOk());
    }

    @Test
    void faceValueBetween_invalid_400() throws Exception {
        mvc.perform(get("/api/bonds/face-value-between?min-value=1500&max-value=500"))
           .andExpect(status().isBadRequest());
    }

    @Test
    void status_ok_withEmptyHeader() throws Exception {
        Mockito.when(service.getBondsByStatus("Active")).thenReturn(List.of());
        mvc.perform(get("/api/bonds/status?status=Active"))
           .andExpect(status().isOk())
           .andExpect(header().string("X-Message", containsString("No bonds")))
           .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void status_invalid_400() throws Exception {
           Mockito.when(service.getBondsByStatus("Weird"))
                         .thenThrow(new BadRequestException("Invalid status"));
           mvc.perform(get("/api/bonds/status?status=Weird"))
                         .andExpect(status().isBadRequest())
                         .andExpect(jsonPath("$.error", is("Bad Request")));
    }

    
    @Test
    void issuedBetween_startAfterEnd_400() throws Exception {
           mvc.perform(get("/api/bonds/issued-between?start-date=2025-01-01&end-date=2024-01-01"))
                         .andExpect(status().isBadRequest());
    }

    
    @Test
    void maturingBetween_badIso_400() throws Exception {
           mvc.perform(get("/api/bonds/maturing-between/notadate/2029-01-01"))
                         .andExpect(status().isBadRequest());
    }


    @Test
    void summary_ok() throws Exception {
        Mockito.when(service.getSummary()).thenReturn(Map.of("totalBonds", 2));
        mvc.perform(get("/api/bonds/summary"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.totalBonds", is(2)));
    }
}
