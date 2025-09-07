package com.folaolaitan.bondcatalog.customexceptions;


// Custom exception to be thrown when a requested resource/bond is not found
// This exception can be used to indicate that a specific bond was not found in the database.
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
