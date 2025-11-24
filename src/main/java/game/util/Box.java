package game.util;

import java.util.Objects;

/**
 * Simple generic container.
 */
public class Box<T> {
    private T value;
    private String label;
    private int version;
    private boolean locked;

    public Box(T value) {
        this(value, "default", 1, false);
    }

    public Box(T value, String label, int version, boolean locked) {
        this.value = value;
        this.label = label;
        this.version = version;
        this.locked = locked;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        if (!locked) {
            this.value = value;
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String describe() {
        return String.valueOf(value);
    }

    public static <U> Box<U> of(U value) {
        return new Box<>(value);
    }

    public static <U> boolean compare(Box<? extends U> left, Box<? extends U> right) {
        return Objects.equals(left.getValue(), right.getValue());
    }
}
