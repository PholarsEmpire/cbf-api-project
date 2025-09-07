package com.folaolaitan.bondcatalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

// Base class for different types of financial assets. My bonds, stocks, and other assets will extend this class.
// It provides a common structure and behavior for all asset types.

@Entity
@Inheritance(strategy = InheritanceType.JOINED)           // tables: asset + bond
@DiscriminatorColumn(name = "asset_type")                // stores “BOND” for bonds, STOCK for Stocks, REIT for Real Estate Investments etc.
public abstract class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    // Common fields across all assets
    @Column(nullable = false)
    @NotBlank(message = "Name is mandatory")
    protected String name;  
    
    @Column(nullable = false)
    @NotBlank(message = "Issuer is mandatory")
    protected String issuer;

    @Column(nullable = false)
    @NotBlank(message = "Rating is mandatory")
    protected String rating;

    @Column(nullable = false)
    @NotNull(message = "Issue date is mandatory")
    protected LocalDate issueDate;

    @Column(nullable = false)
    @NotNull(message = "Maturity date is mandatory")
    protected LocalDate maturityDate;

    @Column(nullable = false)
    @NotBlank(message = "Currency is mandatory")
    protected String currency;

    //Default Constructor
    public Asset() {}  

    // Parameterized constructor
    public Asset(Long id, String name, String issuer, String rating,
                 LocalDate issueDate, LocalDate maturityDate, String currency) {
        this.id = id;
        this.name = name;
        this.issuer = issuer;
        this.rating = rating;
        this.issueDate = issueDate;
        this.maturityDate = maturityDate;
        this.currency = currency;
    }

    // Getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public LocalDate getMaturityDate() { return maturityDate; }
    public void setMaturityDate(LocalDate maturityDate) { this.maturityDate = maturityDate; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
