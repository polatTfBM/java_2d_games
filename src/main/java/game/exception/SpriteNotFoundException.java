package game.exception;

/**
 * Thrown when a requested sprite cannot be created.
 */
public class SpriteNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String reason;

    public SpriteNotFoundException(String message) {
        super(message);
        this.reason = message;
    }

    public SpriteNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.reason = message;
    }

    public String getReason() {
        return reason;
    }
}
