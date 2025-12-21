package util;

public class InputValidator {
    private InputValidator() {
    }

    public static void requireNonEmpty(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requirePositive(int value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
