package com.folaolaitan.bondcatalog.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//import com.folaolaitan.bondcatalog.dto.BondSummary;
import com.folaolaitan.bondcatalog.entity.Bond;


public interface BondRepository extends JpaRepository<Bond, Long> {
    
    
    // Custom query methods for Bond entity
    // List<Bond> findByIsTaxExempt(Boolean isTaxExempt) to find tax-exempt bonds


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


//    @Query("""
//         SELECT new com.folaolaitan.bondcatalog.dto.BondSummary(
//             COUNT(b), 
//             AVG(b.faceValue), 
//             AVG(b.couponRate), 
//             MAX(b.couponRate), 
//             MAX(b.rating), 
//             COUNT(DISTINCT b.issuer), 
//             MAX(b.maturityDate), 
//             MAX(b.issueDate)
//         )
//         FROM Bond b
//     """)
//     BondSummary summaryOfBonds();


    // @Query("select avg(b.faceValue) as averageFaceValue, " +
    //        "avg(b.couponRate) as averageCouponRate, " +
    //        "max(b.couponRate) as maxCouponRate, " +
    //        "max(b.rating) as maxRating, " +
    //        "count(distinct b.issuer) as uniqueIssuers, " +
    //        "max(b.maturityDate) as latestMaturityDate, " +
    //        "max(b.issueDate) as latestIssueDate " +
    //        "from Bond b")
    // Object[] basicBondStats();


    //This section contains queries to be used for the bond analysis/basic statistics

    //@Query("select b from Bond b where b.Status='Active'")
    //@Query("select b from Bond b where b.Status='Matured'")


    @Query("select avg(b.couponRate) from Bond b")
    BigDecimal avgCouponRate();

    @Query("select max(b.rating) from Bond b")
    String maxRating();

    @Query("select count(distinct b.issuer) from Bond b")
    String uniqueIssuers();

    Bond findTopByOrderByCouponRateDesc();              // highest coupon
    Bond findBottomByOrderByCouponRateDesc();           // lowest coupon
    Bond findTopByOrderByMaturityDateAsc();             // earliest maturity

    long countByMaturityDateBetween(LocalDate start, LocalDate end);
}
