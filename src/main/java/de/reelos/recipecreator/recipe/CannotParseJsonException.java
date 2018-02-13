package de.reelos.recipecreator.recipe;

import java.io.IOException;

/**
 * A general exception indicating a problem while reading or parsing provided json.
 * 
 * @author HerrLock
 */
public class CannotParseJsonException extends IOException {

    /**
     * @deprecated You should use another constructor to pass context.
     */
    @Deprecated
    public CannotParseJsonException() {
        super();
    }

    public CannotParseJsonException( final String message ) {
        super( message );
    }

    public CannotParseJsonException( final Throwable cause ) {
        super( cause );
    }

    public CannotParseJsonException( final String message, final Throwable cause ) {
        super( message, cause );
    }

}
