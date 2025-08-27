package com.folaolaitan.bondcatalog.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)           // tables: asset + bond
@DiscriminatorColumn(name = "asset_type")                // stores “BOND” for bonds, STOCK for Stocks, REIT for Real Estate Investments etc.
public abstract class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    // Common fields across all assets
    protected String name;                
    protected String issuer;              
    protected String rating;              
    protected LocalDate issueDate;
    protected LocalDate maturityDate;
    protected String currency;            // optional but nice: “USD”, “GBP”, "NGN"

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
