package com.saglik.exception;

/**
 * Veri doğrulama sırasında ortaya çıkan hataları temsil eder.
 */
public class DataValidationException extends RuntimeException {
    public DataValidationException(String message) {
        super(message);
    }
}
