package com.folaolaitan.bondcatalog.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.folaolaitan.bondcatalog.customexceptions.ResourceNotFoundException;
import com.folaolaitan.bondcatalog.customexceptions.BadRequestException;
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

    public Bond getBondById(Long id) {
        return bondRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bond with id " + id + " not found."));
    }


    public Bond createBond(Bond bond) {
        if (bond.getId() != null) {
            throw new BadRequestException("Bond ID must be null when creating a new bond.");
        }
        return bondRepository.save(bond);
    }


    public Bond updateBond(Long id, Bond updatedBond) {
        Bond bond = bondRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bond with id " + id + " not found."));

        bond.setName(updatedBond.getName());
        bond.setIssuer(updatedBond.getIssuer());
        bond.setFaceValue(updatedBond.getFaceValue());
        bond.setCouponRate(updatedBond.getCouponRate());
        bond.setRating(updatedBond.getRating());
        bond.setMaturityDate(updatedBond.getMaturityDate());

        return bondRepository.save(bond);
    }


   public void deleteBond(Long id) {
        if (!bondRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bond with id " + id + " not found.");
        }
        bondRepository.deleteById(id);
    }


    public List<Bond> findBondsByIssuer(String issuer) {
        if (issuer == null || issuer.trim().isEmpty()) {
            throw new BadRequestException("Issuer parameter cannot be null or empty.");
        } 
        return bondRepository.findByIssuerContainingIgnoreCase(issuer);
    }

    public List<Bond> findBondsByRating(String rating) {
        if (rating == null || rating.trim().isEmpty()) {
            throw new BadRequestException("Rating parameter cannot be null or empty.");
        }
        return bondRepository.findByRating(rating);
    }

    public List<Bond> findBondsByCouponRateGreaterThanEqual(BigDecimal rate) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Rate must be a non-negative value and greater than 0.");
        }
        return bondRepository.findByCouponRateGreaterThanEqual(rate);
    }


    public List<Bond> findBondsByCouponRateBetween(Double minRate, Double maxRate) {
        if (minRate > maxRate) {
            throw new BadRequestException("Minimum rate cannot be greater than maximum rate.");
        }
        return bondRepository.findByCouponRateBetween(minRate, maxRate);
    }


    public List<Bond> findBondsByMaturityDateBetween(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Start date cannot be after end date.");
        }
        return bondRepository.findByMaturityDateBetween(start, end);
    }

    public List<Bond> findBondsByMaturityDateAfter(LocalDate date) {
        if (date == null || date.isBefore(LocalDate.now())) {
            throw new BadRequestException("Date parameter cannot be null or in the past.");
        }
        return bondRepository.findByMaturityDateAfter(date);
    }

    public List<Bond> findBondsByIssueDateAfter(LocalDate date) {
        if (date == null || date.isAfter(LocalDate.now())) {
            throw new BadRequestException("Date parameter cannot be null or in the future.");
        }
        return bondRepository.findByIssueDateAfter(date);
    }

    public List<Bond> findBondsByIssueDateBetween(LocalDate startDate, LocalDate endDate) {
        if(startDate.isAfter(endDate) || startDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("Start date cannot be after end date. And both dates must be in the past (i.e., before today).");
        }
        return bondRepository.findByIssueDateBetween(startDate, endDate);
    }

    public List<Bond> findBondsByFaceValueGreaterThanEqual(BigDecimal faceValue) {
        if (faceValue == null || faceValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Face value must be a non-negative value and greater than 0.");
        }
        return bondRepository.findByFaceValueGreaterThanEqual(faceValue);
    }

    public List<Bond> findBondsByFaceValueBetween(BigDecimal minValue, BigDecimal maxValue) {
        if (minValue == null || maxValue == null 
        || minValue.compareTo(maxValue) > 0 
        || minValue.compareTo(BigDecimal.ZERO) <= 0 
        || maxValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Invalid input! Please note minValue must be less than maxValue and both must be greater than 0.");
        }
        return bondRepository.findByFaceValueBetween(minValue, maxValue);
    }

   public List<Bond> getBondsByStatus(String status) {
        if (!(status.equalsIgnoreCase("Active")
        || status.equalsIgnoreCase("Matured")
        || status.equalsIgnoreCase("Defaulted"))) {
            throw new BadRequestException("Invalid status. Allowed values: Active, Matured, Defaulted.");
        }
        return bondRepository.findByStatus(status);
    }


    
    public Map<String, Object> getSummary() {
        long totalBonds = bondRepository.count();
        BigDecimal avgCoupon = bondRepository.avgCouponRate();
        //String maxRating = bondRepository.maxRating();
        Long uniqueIssuersCount = bondRepository.uniqueIssuersCount();
        Bond highestCouponBond = bondRepository.findTopByOrderByCouponRateDesc();
        Bond lowestCouponBond = bondRepository.findTopByOrderByCouponRateAsc();
        Bond nextMaturityBond = bondRepository.findTopByOrderByMaturityDateAsc();

        LocalDate today = LocalDate.now();
        long maturitiesNext90d = bondRepository.countByMaturityDateBetween(today, today.plusDays(90));

        // Prepare summary map
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalBonds", totalBonds);
        summary.put("avgCouponRate", avgCoupon);
        //summary.put("maxRating", maxRating);
        summary.put("uniqueIssuers", uniqueIssuersCount);
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
