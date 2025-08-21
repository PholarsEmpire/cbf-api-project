package com.folaolaitan.bondcatalog.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

//import com.folaolaitan.bondcatalog.dto.BondSummary;
import com.folaolaitan.bondcatalog.entity.Bond;
import com.folaolaitan.bondcatalog.repository.BondRepository;

@Service
public class BondService {
    private final BondRepository bondRepository;

    public BondService(BondRepository bondRepository) {
        this.bondRepository = bondRepository;
    }

    // Methods to handle business logic related to bonds can be added here
    // For example, methods to create, update, delete, and retrieve bonds
    public List<Bond> getAllBonds() {
        return bondRepository.findAll();
    }

    public Optional<Bond> getBondById(Long id) {
        return bondRepository.findById(id);
    }

    public Bond createBond(Bond bond) {
        try {
            if (bond.getId() != null) {
                throw new IllegalArgumentException("Bond ID should be null for new bonds");
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Bond creation failed: " + e.getMessage());
        }
        return bondRepository.save(bond);
    }

    public Bond updateBond(Long id, Bond updatedBond) {
        Bond bond = bondRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bond not found"));
        bond.setName(updatedBond.getName());
        bond.setIssuer(updatedBond.getIssuer());
        bond.setFaceValue(updatedBond.getFaceValue());
        bond.setCouponRate(updatedBond.getCouponRate());
        bond.setRating(updatedBond.getRating());
        bond.setMaturityDate(updatedBond.getMaturityDate());
        return bondRepository.save(bond);
    }

    public void deleteBond(Long id) {
        bondRepository.deleteById(id);
    }

    public List<Bond> findBondsByIssuer(String issuer) {
        return bondRepository.findByIssuerContainingIgnoreCase(issuer);
    }

    public List<Bond> findBondsByRating(String rating) {
        return bondRepository.findByRating(rating);
    }

    public List<Bond> findBondsByCouponRateGreaterThanEqual(BigDecimal rate) {
        return bondRepository.findByCouponRateGreaterThanEqual(rate);
    }

    public List<Bond> findBondsByCouponRateBetween(Double minRate, Double maxRate) {
        return bondRepository.findByCouponRateBetween(minRate, maxRate);
    }

    public List<Bond> findBondsByMaturityDateBetween(LocalDate start, LocalDate end) {
        return bondRepository.findByMaturityDateBetween(start, end);
    }

    public List<Bond> findBondsByMaturityDateAfter(LocalDate date) {
        return bondRepository.findByMaturityDateAfter(date);
    }

    public List<Bond> findBondsByIssueDateAfter(LocalDate date) {
        return bondRepository.findByIssueDateAfter(date);
    }

    public List<Bond> findBondsByIssueDateBetween(LocalDate startDate, LocalDate endDate) {
        return bondRepository.findByIssueDateBetween(startDate, endDate);
    }

    public List<Bond> findBondsByFaceValueGreaterThanEqual(BigDecimal faceValue) {
        return bondRepository.findByFaceValueGreaterThanEqual(faceValue);
    }

    public List<Bond> findBondsByFaceValueBetween(BigDecimal minValue, BigDecimal maxValue) {
        return bondRepository.findByFaceValueBetween(minValue, maxValue);
    }

    public List<Bond> getBondsByStatus(String status) {
        return bondRepository.findByStatus(status);
    }



    // public BondSummary getBondSummary() {
    //     return bondRepository.summaryOfBonds();
    // }

    
    public Map<String, Object> getSummary() {
        long totalBonds = bondRepository.count();
        BigDecimal avgCoupon = bondRepository.avgCouponRate();
        String maxRating = bondRepository.maxRating();
        String uniqueIssuers = bondRepository.uniqueIssuers();
        Bond highestCouponBond = bondRepository.findTopByOrderByCouponRateDesc();
        Bond lowestCouponBond = bondRepository.findBottomByOrderByCouponRateDesc();
        Bond nextMaturityBond = bondRepository.findTopByOrderByMaturityDateAsc();

        LocalDate today = LocalDate.now();
        long maturitiesNext90d = bondRepository.countByMaturityDateBetween(today, today.plusDays(90));

        // Prepare summary map
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalBonds", totalBonds);
        summary.put("avgCouponRate", avgCoupon);
        summary.put("maxRating", maxRating);
        summary.put("uniqueIssuers", uniqueIssuers);
        summary.put("highestCoupon", highestCouponBond != null ? highestCouponBond.getCouponRate() : null);
        summary.put("lowestCoupon", lowestCouponBond != null ? lowestCouponBond.getCouponRate() : null);
        summary.put("highestCouponBondName", highestCouponBond != null ? highestCouponBond.getName() : null);
        summary.put("lowestCouponBondName", lowestCouponBond != null ? lowestCouponBond.getName() : null);
        summary.put("nextMaturityBondName", nextMaturityBond != null ? nextMaturityBond.getName() : null);
        summary.put("nextMaturityBondDate", nextMaturityBond != null ? nextMaturityBond.getMaturityDate() : null);
        summary.put("maturitiesInNext90Days", maturitiesNext90d);
        return summary;
  }
}
