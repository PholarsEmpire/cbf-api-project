package com.folaolaitan.bondcatalog.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a bond entity with its attributes.
 */
@Entity
@Table(name = "bonds")
public class Bond {
    // Unique identifier for the bond
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Attributes of the bond
    private String name;
    private String issuer;
    private BigDecimal faceValue;
    private BigDecimal couponRate;
    private String rating;
    private LocalDate issueDate;
    private LocalDate maturityDate;


    public Bond() {
        // Default constructor for JP
    
    }

    // Constructor to initialize all fields
    public Bond(Long id, String name, String issuer, BigDecimal faceValue, BigDecimal couponRate, String rating, LocalDate issueDate, LocalDate maturityDate) {
        this.id = id;
        this.name = name;
        this.issuer = issuer;
        this.faceValue = faceValue;
        this.couponRate = couponRate;
        this.rating = rating;
        this.issueDate = issueDate;
        this.maturityDate = maturityDate;
    }
    
 
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIssuer() {
        return issuer;
    }
    public void setIssuer(String issuer) {
        this.issuer = issuer;
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
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public LocalDate getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
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
