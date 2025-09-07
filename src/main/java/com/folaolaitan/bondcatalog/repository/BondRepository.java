package com.folaolaitan.bondcatalog.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.folaolaitan.bondcatalog.entity.Bond;

// Repository interface for Bond entity. It provides methods to perform CRUD operations and custom queries.
// It's like the database access layer for Bond entities.
public interface BondRepository extends JpaRepository<Bond, Long> {
    // Check if a bond exists with the given name, issuer, and maturity date. This will avoid duplicates
    boolean existsByNameAndIssuerAndMaturityDate(String name, String issuer, LocalDate maturityDate);
    
    // Custom query methods for Bond entity
    List<Bond> findByIssuerContainingIgnoreCase(String issuer);
    List<Bond> findByRating(String rating); // to find bonds with a specific credit rating
    List<Bond> findByCouponRateGreaterThanEqual(BigDecimal rate); // to find bonds with a coupon rate greater than or equal to a certain rate
    List<Bond> findByCouponRateBetween(Double minRate, Double maxRate); // to find bonds within a certain coupon rate range
    List<Bond> findByMaturityDateBetween(LocalDate start, LocalDate end); //to find bonds maturing within a specific date range
    List<Bond> findByMaturityDateAfter(LocalDate date); // to find bonds maturing after a certain date
    List<Bond> findByIssueDateAfter(LocalDate date); // to find bonds issued after a certain date
    List<Bond> findByIssueDateBetween(LocalDate startDate, LocalDate endDate); // to find bonds issued within a specific date range
    List<Bond> findByFaceValueGreaterThanEqual(BigDecimal faceValue); // to find bonds with a face value greater than or equal to a certain amount
    List<Bond> findByFaceValueBetween(BigDecimal minValue, BigDecimal maxValue); // to find bonds within a certain face value range
    List<Bond> findByStatus(String status); // to find bonds with a specific status (e.g., "Active", "Matured", "Defaulted")


    //summary statistics queries
    @Query("select avg(b.couponRate) from Bond b")
    BigDecimal avgCouponRate();

    @Query("select max(b.rating) from Bond b")
    String maxRating();

    @Query("select count(distinct b.issuer) from Bond b")
    Long uniqueIssuersCount();

    Bond findTopByOrderByCouponRateDesc();              // highest coupon
    Bond findTopByOrderByCouponRateAsc();           // lowest coupon
    Bond findTopByOrderByMaturityDateAsc();             // earliest maturity

    long countByMaturityDateBetween(LocalDate start, LocalDate end);
}
