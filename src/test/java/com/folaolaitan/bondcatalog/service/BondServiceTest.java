package com.folaolaitan.bondcatalog.service;

import com.folaolaitan.bondcatalog.customexceptions.BadRequestException;
import com.folaolaitan.bondcatalog.customexceptions.ResourceNotFoundException;
import com.folaolaitan.bondcatalog.entity.Bond;
import com.folaolaitan.bondcatalog.repository.BondRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BondServiceTest {

    @Mock BondRepository repo;
    @InjectMocks BondService service;

    Bond bond;

    @BeforeEach
    void setUp() {
        bond = new Bond(1L, "US Treasury 10Y", "US Government",
                new BigDecimal("1000"), new BigDecimal("3.50"), "AAA",
                LocalDate.of(2020,1,1), LocalDate.of(2030,1,1), "USD");
    }

    // ---- CRUD ----

    @Test
    void getAllBonds_returnsList() {
        when(repo.findAll()).thenReturn(List.of(bond));
        assertThat(service.getAllBonds()).hasSize(1);
        verify(repo).findAll();
    }

    @Test
    void getBondById_found() {
        when(repo.findById(1L)).thenReturn(Optional.of(bond));
        assertThat(service.getBondById(1L).getName()).isEqualTo("US Treasury 10Y");
    }

    @Test
    void getBondById_notFound_throws() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getBondById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createBond_ok_whenIdNull() {
        Bond incoming = new Bond(null, "Corp A 2028", "Corp A",
                new BigDecimal("1000"), new BigDecimal("5.00"), "AA",
                LocalDate.of(2023,1,1), LocalDate.of(2028,1,1), "USD");
        when(repo.save(any(Bond.class))).thenAnswer(inv -> {
            Bond b = inv.getArgument(0);
            return new Bond(10L, b.getName(), b.getIssuer(), b.getFaceValue(), b.getCouponRate(),
                    b.getRating(), b.getIssueDate(), b.getMaturityDate(), "USD");
        });
        Bond saved = service.createBond(incoming);
        assertThat(saved.getId()).isEqualTo(10L);
    }

    @Test
    void createBond_idPresent_throwsBadRequest() {
        Bond incoming = new Bond(5L, "X", "Y", new BigDecimal("1000"),
                new BigDecimal("2.0"), "BBB", LocalDate.now(), LocalDate.now().plusYears(1), "USD");
        assertThatThrownBy(() -> service.createBond(incoming))
                .isInstanceOf(BadRequestException.class);
        verify(repo, never()).save(any());
    }

    @Test
    void updateBond_ok() {
        when(repo.findById(1L)).thenReturn(Optional.of(bond));
        when(repo.save(any(Bond.class))).thenAnswer(inv -> inv.getArgument(0));
        Bond patch = new Bond(null, "Updated", "IssuerX",
                new BigDecimal("2000"), new BigDecimal("4.00"), "AA",
                LocalDate.of(2021,1,1), LocalDate.of(2031,1,1), "USD");
        Bond updated = service.updateBond(1L, patch);
        assertThat(updated.getName()).isEqualTo("Updated");
        assertThat(updated.getIssuer()).isEqualTo("IssuerX");
        assertThat(updated.getFaceValue()).isEqualByComparingTo("2000");
        assertThat(updated.getCouponRate()).isEqualByComparingTo("4.00");
        assertThat(updated.getRating()).isEqualTo("AA");
        assertThat(updated.getMaturityDate()).isEqualTo(LocalDate.of(2031,1,1));
    }

    @Test
    void updateBond_notFound_throws() {
        when(repo.findById(2L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.updateBond(2L, bond))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteBond_ok() {
        when(repo.existsById(1L)).thenReturn(true);
        service.deleteBond(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    void deleteBond_notFound_throws() {
        when(repo.existsById(1L)).thenReturn(false);
        assertThatThrownBy(() -> service.deleteBond(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ---- FINDERS & VALIDATION ----

    @Test
    void findBondsByIssuer_ok() {
        when(repo.findByIssuerContainingIgnoreCase("gov")).thenReturn(List.of(bond));
        assertThat(service.findBondsByIssuer("gov")).hasSize(1);
    }

    @Test
    void findBondsByIssuer_blank_throws() {
        assertThatThrownBy(() -> service.findBondsByIssuer("  "))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void findBondsByRating_ok() {
        when(repo.findByRating("AAA")).thenReturn(List.of(bond));
        assertThat(service.findBondsByRating("AAA")).hasSize(1);
    }

    @Test
    void findBondsByRating_blank_throws() {
        assertThatThrownBy(() -> service.findBondsByRating(" "))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void findByCouponRateGreaterThanEqual_ok() {
        when(repo.findByCouponRateGreaterThanEqual(new BigDecimal("3.0"))).thenReturn(List.of(bond));
        assertThat(service.findBondsByCouponRateGreaterThanEqual(new BigDecimal("3.0"))).hasSize(1);
    }

    @Test
    void findByCouponRateGreaterThanEqual_invalid_throws() {
        assertThatThrownBy(() -> service.findBondsByCouponRateGreaterThanEqual(null))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.findBondsByCouponRateGreaterThanEqual(new BigDecimal("0")))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.findBondsByCouponRateGreaterThanEqual(new BigDecimal("-1")))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void findByCouponRateBetween_ok() {
        when(repo.findByCouponRateBetween(3.0, 6.0)).thenReturn(List.of(bond));
        assertThat(service.findBondsByCouponRateBetween(3.0, 6.0)).hasSize(1);
    }

    @Test
    void findByCouponRateBetween_minGtMax_throws() {
        assertThatThrownBy(() -> service.findBondsByCouponRateBetween(6.0, 3.0))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void findByMaturityDateBetween_ok() {
        LocalDate s = LocalDate.of(2029,1,1);
        LocalDate e = LocalDate.of(2031,1,1);
        when(repo.findByMaturityDateBetween(s, e)).thenReturn(List.of(bond));
        assertThat(service.findBondsByMaturityDateBetween(s, e)).hasSize(1);
    }

    @Test
    void findByMaturityDateBetween_startAfterEnd_throws() {
        LocalDate s = LocalDate.of(2031,1,1);
        LocalDate e = LocalDate.of(2029,1,1);
        assertThatThrownBy(() -> service.findBondsByMaturityDateBetween(s, e))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void findByMaturityDateAfter_ok() {
        LocalDate d = LocalDate.now().plusDays(1);
        when(repo.findByMaturityDateAfter(d)).thenReturn(List.of(bond));
        assertThat(service.findBondsByMaturityDateAfter(d)).hasSize(1);
    }

    @Test
    void findByMaturityDateAfter_nullOrPast_throws() {
        assertThatThrownBy(() -> service.findBondsByMaturityDateAfter(null))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.findBondsByMaturityDateAfter(LocalDate.now().minusDays(1)))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void findByIssueDateAfter_ok() {
        LocalDate d = LocalDate.now().minusDays(1);
        when(repo.findByIssueDateAfter(d)).thenReturn(List.of(bond));
        assertThat(service.findBondsByIssueDateAfter(d)).hasSize(1);
    }

    @Test
    void findByIssueDateAfter_nullOrFuture_throws() {
        assertThatThrownBy(() -> service.findBondsByIssueDateAfter(null))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.findBondsByIssueDateAfter(LocalDate.now().plusDays(1)))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void findByIssueDateBetween_ok() {
        LocalDate s = LocalDate.of(2019,1,1);
        LocalDate e = LocalDate.of(2021,1,1);
        when(repo.findByIssueDateBetween(s, e)).thenReturn(List.of(bond));
        assertThat(service.findBondsByIssueDateBetween(s, e)).hasSize(1);
    }

    @Test
    void findByIssueDateBetween_startAfterEnd_throws() {
        LocalDate s = LocalDate.of(2021,1,1);
        LocalDate e = LocalDate.of(2019,1,1);
        assertThatThrownBy(() -> service.findBondsByIssueDateBetween(s, e))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void findByFaceValueGte_ok() {
        when(repo.findByFaceValueGreaterThanEqual(new BigDecimal("1000"))).thenReturn(List.of(bond));
        assertThat(service.findBondsByFaceValueGreaterThanEqual(new BigDecimal("1000"))).hasSize(1);
    }

    @Test
    void findByFaceValueGte_invalid_throws() {
        assertThatThrownBy(() -> service.findBondsByFaceValueGreaterThanEqual(null))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.findBondsByFaceValueGreaterThanEqual(new BigDecimal("0")))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void findByFaceValueBetween_ok() {
        when(repo.findByFaceValueBetween(new BigDecimal("500"), new BigDecimal("1500"))).thenReturn(List.of(bond));
        assertThat(service.findBondsByFaceValueBetween(new BigDecimal("500"), new BigDecimal("1500"))).hasSize(1);
    }

    @Test
    void findByFaceValueBetween_invalid_throws() {
        assertThatThrownBy(() -> service.findBondsByFaceValueBetween(null, new BigDecimal("1")))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.findBondsByFaceValueBetween(new BigDecimal("2"), new BigDecimal("1")))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> service.findBondsByFaceValueBetween(new BigDecimal("0"), new BigDecimal("1")))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void getBondsByStatus_ok() {
        when(repo.findByStatus("Active")).thenReturn(List.of(bond));
        assertThat(service.getBondsByStatus("Active")).hasSize(1);
    }

    @Test
    void getBondsByStatus_invalid_throws() {
        assertThatThrownBy(() -> service.getBondsByStatus("Weird"))
                .isInstanceOf(BadRequestException.class);
    }

    // ---- Summary ----

    @Test
    void summary_ok() {
        when(repo.count()).thenReturn(2L);
        when(repo.avgCouponRate()).thenReturn(new BigDecimal("3.75"));
        when(repo.uniqueIssuersCount()).thenReturn(2L);
        when(repo.findTopByOrderByCouponRateDesc()).thenReturn(bond);
        when(repo.findTopByOrderByCouponRateAsc()).thenReturn(bond);
        when(repo.findTopByOrderByMaturityDateAsc()).thenReturn(bond);
        when(repo.countByMaturityDateBetween(any(), any())).thenReturn(1L);

        Map<String, Object> s = service.getSummary();
        assertThat(s.get("totalBonds")).isEqualTo(2L);
        assertThat(s.get("avgCouponRate")).isEqualTo(new BigDecimal("3.75"));
        assertThat(s.get("uniqueIssuers")).isEqualTo(2L);
        assertThat(s.get("highestCoupon")).isEqualTo(bond.getCouponRate());
        assertThat(s.get("lowestCoupon")).isEqualTo(bond.getCouponRate());
        assertThat(s.get("nextMaturityBondName")).isEqualTo(bond.getName());
    }
}
