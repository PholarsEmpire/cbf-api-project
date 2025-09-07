package com.folaolaitan.bondcatalog.controller;

import com.folaolaitan.bondcatalog.entity.Bond;
import com.folaolaitan.bondcatalog.customexceptions.BadRequestException;
import com.folaolaitan.bondcatalog.service.BondService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;



// Controller for managing bond-related operations. 
// This is the main entry point for the Bond API where requests are received from clients (e.g frontend, Swagger UI or Postman) and responses are sent.
@RestController
@RequestMapping("/api/bonds")
@Tag(name = "Fixed Income (Bond) Catalog API",
     description = "Endpoints for managing fixed-income/bond investments")
@CrossOrigin(origins = "http://localhost:5173") // This will allow my frontend to access the API
public class BondController {

    private final BondService service;

    public BondController(BondService service) {
        this.service = service;
    }

    // ---------- CORE CRUD ----------

    // List all bonds
    @GetMapping
    @Operation(summary = "List all bonds", description = "Retrieves every bond in the catalog.")
    public List<Bond> getAllBonds() {
        return service.getAllBonds();
    }

    // Get a bond by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get a bond by ID",
               description = "Returns the bond with the given ID, or 404 if not found.")
    @ApiResponse(responseCode = "200", description = "Bond found")
    @ApiResponse(responseCode = "404", description = "Bond not found")
    public Bond getBond(@PathVariable Long id) {
        return service.getBondById(id); // throws ResourceNotFoundException if missing
    }

    // Endpoint to create a new bond
    @PostMapping
    @Operation(summary = "Create a new bond",
               description = "Creates a bond. ID must be null; DB will generate it.")
    @ApiResponse(responseCode = "200", description = "Bond created")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public Bond createBond(@RequestBody Bond bond) {
        return service.createBond(bond); // throws BadRequestException if ID not null
    }

    // Endpoint to update an existing bond
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing bond",
               description = "Updates the bond with the given ID.")
    @ApiResponse(responseCode = "200", description = "Bond updated")
    @ApiResponse(responseCode = "404", description = "Bond not found")
    public Bond updateBond(@PathVariable Long id, @RequestBody Bond bond) {
        return service.updateBond(id, bond);
    }

    // Delete a bond
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a bond", description = "Deletes the bond with the given ID.")
    @ApiResponse(responseCode = "200", description = "Bond deleted")
    @ApiResponse(responseCode = "404", description = "Bond not found")
    public void deleteBond(@PathVariable Long id) {
        service.deleteBond(id);
    }


    // ---------- SEARCH / FILTER ----------
    // These custom endpoints allow clients to search for bonds based on various criteria.

    // Search bonds by issuer
    @GetMapping("/issuer/{issuer}")
    @Operation(summary = "Search bonds by issuer",
               description = "Case-insensitive contains search by issuer name.")
    public List<Bond> findBondsByIssuer(
            @Parameter(description = "Issuer name to search for", example = "Apple Inc")
            @PathVariable String issuer) {
        return service.findBondsByIssuer(issuer);
    }

    // Filter bonds by rating
    @GetMapping("/rating/{rating}")
    @Operation(summary = "Filter bonds by rating",
               description = "Returns all bonds that match the given credit rating.")
    public List<Bond> findBondsByRating(
            @Parameter(description = "Rating to filter", example = "AAA")
            @PathVariable String rating) {
        return service.findBondsByRating(rating);
    }

    // Filter bonds by coupon rate greater than or equal to a value
    @GetMapping("/coupon-rate/{rate}")
    @Operation(summary = "Find bonds with coupon ≥ rate",
               description = "Returns bonds with coupon rate greater than or equal to the given value.")
    public List<Bond> findBondsByCouponRateGreaterThanEqual(
            @Parameter(description = "Minimum coupon rate", example = "5.50")
            @PathVariable BigDecimal rate) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Rate must be > 0.");
        }
        return service.findBondsByCouponRateGreaterThanEqual(rate);
    }

    // Filter bonds by coupon rate range
    @GetMapping("/coupon-rate/{min}/{max}")
    @Operation(summary = "Find bonds by coupon rate range",
               description = "Returns bonds with coupon rate between min and max (inclusive).")
    public List<Bond> findBondsByCouponRateBetween(
            @Parameter(description = "Minimum coupon rate", example = "3.00")
            @PathVariable("min") Double minRate,
            @Parameter(description = "Maximum coupon rate", example = "8.00")
            @PathVariable("max") Double maxRate) {
        if (minRate == null || maxRate == null || minRate <= 0 || maxRate <= 0 || minRate > maxRate) {
            throw new BadRequestException("Invalid range. Ensure min/max are > 0 and min ≤ max.");
        }
        return service.findBondsByCouponRateBetween(minRate, maxRate);
    }

    // Find bonds maturing between two dates
    @GetMapping("/maturing-between/{start}/{end}")
    @Operation(summary = "Find bonds maturing between two dates",
               description = "Dates must be ISO format YYYY-MM-DD.")
    public List<Bond> findBondsByMaturityDateBetween(
            @Parameter(description = "Start date", example = "2025-01-01")
            @PathVariable String start,
            @Parameter(description = "End date", example = "2025-12-31")
            @PathVariable String end) {
        try {
            LocalDate s = LocalDate.parse(start);
            LocalDate e = LocalDate.parse(end);
            if (e.isBefore(s)) {
                throw new BadRequestException("End date must be on/after start date.");
            }
            return service.findBondsByMaturityDateBetween(s, e);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Dates must be in ISO format YYYY-MM-DD.");
        }
    }

    // Find bonds maturing after a specific date
    @GetMapping("/maturity-date/{date}")
    @Operation(summary = "Find bonds maturing after a date",
               description = "Date must be ISO format YYYY-MM-DD.")
    public List<Bond> findBondsByMaturityDateAfter(
            @Parameter(description = "Date", example = "2026-12-31")
            @PathVariable String date) {
        try {
            LocalDate d = LocalDate.parse(date);
            return service.findBondsByMaturityDateAfter(d);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Date must be in ISO format YYYY-MM-DD.");
        }
    }

    // Find bonds issued between two dates
    @GetMapping("/issued-between")
    @Operation(summary = "Find bonds issued between two dates",
               description = "Dates must be ISO format YYYY-MM-DD. Uses query params `start-date` and `end-date`.")
    public List<Bond> findBondsByIssueDateBetween(
            @Parameter(description = "Start date", example = "2022-01-01")
            @RequestParam(name = "start-date") String startDate,
            @Parameter(description = "End date", example = "2026-12-31")
            @RequestParam(name = "end-date") String endDate) {
        try {
            LocalDate s = LocalDate.parse(startDate);
            LocalDate e = LocalDate.parse(endDate);
            if (e.isBefore(s)) {
                throw new BadRequestException("End date must be on/after start date.");
            }
            return service.findBondsByIssueDateBetween(s, e);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Dates must be in ISO format YYYY-MM-DD.");
        }
    }

    // Find bonds issued after a specific date
    @GetMapping("/issue-date/{date}")
    @Operation(summary = "Find bonds issued after a date",
               description = "Date must be ISO format YYYY-MM-DD.")
    public List<Bond> findBondsByIssueDateAfter(
            @Parameter(description = "Date", example = "2022-01-01")
            @PathVariable String date) {
        try {
            LocalDate d = LocalDate.parse(date);
            return service.findBondsByIssueDateAfter(d);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Date must be in ISO format YYYY-MM-DD.");
        }
    }

    // Find bonds by face value
    @GetMapping("/face-value/{value}")
    @Operation(summary = "Find bonds with face value ≥ value",
               description = "Returns bonds with face value greater than or equal to the specified amount.")
    public List<Bond> findBondsByFaceValueGreaterThanEqual(
            @Parameter(description = "Minimum face value", example = "1000")
            @PathVariable BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Face value must be > 0.");
        }
        return service.findBondsByFaceValueGreaterThanEqual(value);
    }

    // Find bonds by face value range
    @GetMapping("/face-value-between")
    @Operation(summary = "Find bonds by face value range",
               description = "Uses query params `min-value` and `max-value`. Both must be > 0 and min ≤ max.")
    public List<Bond> findBondsByFaceValueBetween(
            @Parameter(description = "Minimum face value", example = "1000")
            @RequestParam(name = "min-value") BigDecimal minValue,
            @Parameter(description = "Maximum face value", example = "5000")
            @RequestParam(name = "max-value") BigDecimal maxValue) {
        if (minValue == null || maxValue == null
                || minValue.compareTo(maxValue) > 0
                || minValue.compareTo(BigDecimal.ZERO) <= 0
                || maxValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Invalid face value range. Both must be > 0 and min ≤ max.");
        }
        return service.findBondsByFaceValueBetween(minValue, maxValue);
    }

    // Find bonds by status (Active, Matured, Defaulted)
    @GetMapping("/status")
    @Operation(summary = "Find bonds by status",
               description = "Allowed values: Active, Matured, Defaulted. Returns 200 with [] if none.")
    public ResponseEntity<List<Bond>> getBondsByStatus(
            @Parameter(description = "Bond status", example = "Active")
            @RequestParam String status) {
        List<Bond> list = service.getBondsByStatus(status); // service validates values
        if (list.isEmpty()) {
            return ResponseEntity.ok()
                    .header("X-Message", "No bonds found with status: " + status)
                    .body(list);
        }
        return ResponseEntity.ok(list);
    }

    // ---------- SUMMARY ----------
    // Get overall statistics about the bond catalog
    @GetMapping("/summary")
    @Operation(summary = "Get bond catalog summary",
               description = "Provides statistics about the bond catalog, such as total bonds, average coupon rate, highest coupon rate, unique issuers, etc.")
    @ApiResponse(responseCode = "200", description = "Summary returned")
    public Map<String, Object> summary() {
        return service.getSummary();
    }




}
