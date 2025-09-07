package com.folaolaitan.bondcatalog.customexceptions;

// Custom exception to be thrown when a bad request is made
// This exception can be used to indicate validation errors or invalid input.
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

}
