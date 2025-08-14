package com.folaolaitan.bondcatalog.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BondSummary(
    Long totalBonds,
    Double avgFaceValue,
    Double avgCouponRate,
    BigDecimal maxCouponRate,
    String maxRating,
    Long distinctIssuers,
    LocalDate latestMaturityDate,
    LocalDate latestIssueDate
) {}

