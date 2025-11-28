package com.saglik.exception;

/**
 * Dosya işlemleri sırasında fırlatılan hataları temsil eder.
 */
public class DosyaIslemException extends Exception {
    public DosyaIslemException(String message, Throwable cause) {
        super(message, cause);
    }

    public DosyaIslemException(String message) {
        super(message);
    }
}
