package game.exception;

/**
 * Thrown when an object attempts to move outside allowed boundaries.
 */
public class InvalidMovementException extends Exception {
    private static final long serialVersionUID = 1L;
    private final double attemptedX;
    private final double attemptedY;

    public InvalidMovementException(String message) {
        super(message);
        this.attemptedX = 0;
        this.attemptedY = 0;
    }

    public InvalidMovementException(String message, double attemptedX, double attemptedY) {
        super(message);
        this.attemptedX = attemptedX;
        this.attemptedY = attemptedY;
    }

    public double getAttemptedX() {
        return attemptedX;
    }

    public double getAttemptedY() {
        return attemptedY;
    }
}
