package abstract_;

import exception.BusinessException;
import exception.ValidationException;

public abstract class BaseService {

    public void ensureNotNull(Object value, String message) {
        if (value == null) {
            throw new ValidationException(message);
        }
    }

    public void ensureStringNotEmpty(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(message);
        }
    }

    protected abstract void validateBeforeCreate(Object value);

    protected abstract void validateBeforeUpdate(Object value);
}
