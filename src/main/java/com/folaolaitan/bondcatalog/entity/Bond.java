package com.folaolaitan.bondcatalog.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a bond entity with its attributes.
 */
@Entity
@DiscriminatorValue("BOND")
@Table(name = "bonds")
public class Bond extends Asset {

    @Column(nullable = false)
    @NotNull(message = "Face value is mandatory")
    private BigDecimal faceValue;

    @Column(nullable = false)
    @NotNull(message = "Coupon rate is mandatory")
    private BigDecimal couponRate;

    private String status; // e.g., "Active", "Matured", "Defaulted"


    public Bond() {
        // Default constructor for JPA
    }

    // Constructor to initialize all fields
    public Bond(Long id, String name, String issuer, BigDecimal faceValue, BigDecimal couponRate, String rating, LocalDate issueDate, LocalDate maturityDate, String currency) {
        super(id, name, issuer, rating, issueDate, maturityDate, currency);
        this.faceValue = faceValue;
        this.couponRate = couponRate;
        this.status = calculateStatus();
    }

    private String calculateStatus() {
        boolean flaggedDefault = false;    // This will be set in the DB based on some business logic or conditions (e.g., payment history)
        if (flaggedDefault) {
            return "Defaulted";
        } else if (maturityDate.isBefore(LocalDate.now())) {
            return "Matured";
        } else {
            return "Active";
        }
    }


    //getters and setters
    public String getStatus() {
        return status;
    }

    public BigDecimal getFaceValue() {
        return faceValue;
    }
    public void setFaceValue(BigDecimal faceValue) {
        this.faceValue = faceValue;
    }
    public BigDecimal getCouponRate() {
        return couponRate;
    }
    public void setCouponRate(BigDecimal couponRate) {
        this.couponRate = couponRate;
    }
  
    public LocalDate getMaturityDate() {
        return maturityDate;
    }
    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    @Override
    public String toString() {
        return "Bond{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", issuer='" + issuer + '\'' +
                ", faceValue=" + faceValue +
                ", couponRate=" + couponRate +
                ", rating='" + rating + '\'' +
                ", issueDate=" + issueDate +
                ", maturityDate=" + maturityDate +
                '}';
    }
}
