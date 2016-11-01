package de.reelos.recipecreator.util;

import java.io.IOException;

/**
 * @author HerrLock
 */
public class CannotReadJsonException extends IOException {

    /**
     * @deprecated You should use another constructor to pass context.
     */
    @Deprecated
    public CannotReadJsonException() {
        super();
    }

    public CannotReadJsonException( final String message ) {
        super( message );
    }

    public CannotReadJsonException( final Throwable cause ) {
        super( cause );
    }

    public CannotReadJsonException( final String message, final Throwable cause ) {
        super( message, cause );
    }

}
