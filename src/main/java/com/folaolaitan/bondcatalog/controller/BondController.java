package com.folaolaitan.bondcatalog.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.folaolaitan.bondcatalog.dto.BondSummary;
import com.folaolaitan.bondcatalog.entity.Bond;
import com.folaolaitan.bondcatalog.service.BondService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api/bonds")   
@Tag(name = "Fixed Income (Bond) Catalog API", description = "Endpoints for managing fixed income/bond investments in the catalog")
public class BondController {
    // This class will handle HTTP requests related to bonds
    // Methods for creating, updating, deleting, and retrieving bonds will be added here
    // For example:
    // @GetMapping, @PostMapping, @PutMapping, @DeleteMapping annotations can be used to map HTTP methods to these methods

    private final BondService bondService;
    // Constructor injection for BondService
    // This allows the controller to use the service layer for business logic
    public BondController(BondService bondService) {
        this.bondService = bondService;
    }

    @GetMapping
    @Operation(
        summary = "Returns the list of all bonds",
        description = "Retrieves all bonds from the catalog."
    )
    public List<Bond> getAllBonds() {
        return bondService.getAllBonds();
    }



    @GetMapping("/{id}")
    @Operation(
        summary = "Get a specificbond by ID",
        description = "Retrieves a bond by its unique identifier."
    )
    public Bond getBond(@PathVariable Long id) {
        return bondService.getBondById(id).orElseThrow(() -> new RuntimeException("Bond not found"));
    }



    @PostMapping
    @Operation(
        summary = "Creates and adds a new bond to the catalog",
        description = "Adds a new bond to the catalog."
    )
    public Bond createBond(@RequestBody Bond bond) {
        return bondService.createBond(bond);
    }




    @PutMapping("/{id}")
    @Operation(
        summary = "Updates an existing bond using its ID",
        description = "Updates the details of an existing bond in the catalog."
    )
    public Bond updateBond(@PathVariable Long id, @RequestBody Bond bond) {
        return bondService.updateBond(id, bond);
    }



    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletes a bond by ID",
        description = "Removes a bond from the catalog using its unique identifier."
    )
    public void deleteBond(@PathVariable Long id) {
        bondService.deleteBond(id);
    }




    @GetMapping("/issuer/{issuer}")
    @Operation(
        summary = "Search bonds by issuer",
        description = "Finds bonds where the issuer name matches the search keyword."
         )
    @ApiResponse(responseCode = "200", description = "Bonds found successfully")
    public List<Bond> findBondsByIssuer(@Parameter(description = "Issuer name to search for", example = "Apple Inc") @PathVariable String issuer) {
        return bondService.findBondsByIssuer(issuer);
    }




    @GetMapping("/rating/{rating}")
    @Operation(
        summary = "Filters bonds by a specific credit rating",
        description = "Returns all bonds that match the given credit rating."
    )
    public List<Bond> findBondsByRating(@Parameter(description = "Credit rating of the bond to search for", example = "AAA") @PathVariable String rating) {
        return bondService.findBondsByRating(rating);
    }




    @GetMapping("/coupon-rate/{rate}")
    @Operation(
        summary = "Find high-yield bonds",
        description = "Returns all bonds with a coupon rate greater than or equal to the provided value."
    )
    public List<Bond> findBondsByCouponRateGreaterThanEqual(@Parameter(description = "Minimum coupon rate to filter by", example = "5.5") @PathVariable BigDecimal rate) {
        return bondService.findBondsByCouponRateGreaterThanEqual(rate);
    }




    @GetMapping("/coupon-rate/{min-rate}/{max-rate}")
    @Operation(
        summary = "Find bonds within a specific coupon rate range",
        description = "Returns all bonds with a coupon rate between the specified minimum and maximum values."
    )
    public List<Bond> findBondsByCouponRateBetween(Double minRate, Double maxRate) {
        return bondService.findBondsByCouponRateBetween(minRate, maxRate);
    }




    @GetMapping("/maturing-between/{start}/{end}")
    @Operation(
        summary = "Find bonds maturing between two dates",
        description = "Returns all bonds that mature between the specified start and end dates. Format: yyyy-MM-dd"
    )
    public List<Bond> findBondsByMaturityDateBetween(@Parameter(description = "Start date to filter by", example = "2025-01-01") @PathVariable LocalDate start, @Parameter(description = "End date to filter by", example = "2025-12-31") @PathVariable LocalDate end) {
        return bondService.findBondsByMaturityDateBetween(start, end);
    }




    @GetMapping("/maturity-date/{date}")
    @Operation(
        summary = "Find bonds maturing after a specific date",
        description = "Returns all bonds that mature after the specified date."
    )
    public List<Bond> findBondsByMaturityDateAfter(@Parameter(description ="Maturity date to filter by", example = "2026-12-31") @PathVariable LocalDate date) {
        return bondService.findBondsByMaturityDateAfter(date);
    }



    @GetMapping("/issued-between/{start-date}/{end-date}")
    @Operation(
        summary = "Find bonds issued between two dates (a range of dates)",
        description = "Returns all bonds that were issued between the specified start and end dates."
    )
    public List<Bond> findBondsByIssueDateBetween(@Parameter(description ="Start date to filter by", example = "2022-01-01") @PathVariable LocalDate startDate, @Parameter(description ="End date to filter by", example = "2022-12-31") @PathVariable LocalDate endDate) {
        return bondService.findBondsByIssueDateBetween(startDate, endDate);
    }




    @GetMapping("/issue-date/{date}")
    @Operation(
        summary = "Find bonds issued after a specific date",
        description = "Returns all bonds that were issued after the specified date."
    )
    public List<Bond> findBondsByIssueDateAfter(@Parameter(description = "Issue date to filter by", example = "2022-01-01") @PathVariable LocalDate date) {
        return bondService.findBondsByIssueDateAfter(date);
    }




    @GetMapping("/face-value/{value}")
    @Operation(
        summary = "Find bonds with a face value greater than or equal to a specified amount",
        description = "Returns all bonds with a face value greater than or equal to the specified value."
    )
    public List<Bond> findBondsByFaceValueGreaterThanEqual(@Parameter(description = "Face value to filter by", example = "1000") @PathVariable BigDecimal faceValue) {
        return bondService.findBondsByFaceValueGreaterThanEqual(faceValue);
    }




    @GetMapping("/face-value-between/{min-value}/{max-value}")
    @Operation(
        summary = "Find bonds with a face value within a specified range",
        description = "Returns all bonds with a face value between the specified minimum and maximum values."
    )
    public List<Bond> findBondsByFaceValueBetween(@Parameter(description ="Minimum face value to filter by", example = "1000") @PathVariable BigDecimal minValue, @Parameter(description ="Maximum face value to filter by", example = "5000") @PathVariable BigDecimal maxValue) {
        return bondService.findBondsByFaceValueBetween(minValue, maxValue);
    }

   


    @GetMapping("/summary")
    @Operation(
        summary = "Get bonds summary",
        description = "Returns a summary of the bond catalog, including total number of bonds, total face value, and average coupon rate."
    )
    public BondSummary getBondSummary() {
        return bondService.getBondSummary();
    }

}
