package com.meriosol.jaxb;

/**
 * This runtime exception is introduced to shield from frameworks/libs(JAXB, IO etc) checked exceptions in order to client decide if they want exception handling or not.
 *
 * @author meriosol
 * @version 0.1
 * @since 05/04/14
 */
public class JaxbRuntimeException extends RuntimeException {
    public JaxbRuntimeException() {
    }

    /**
     * @param errorMessage Error message
     */
    public JaxbRuntimeException(String errorMessage) {
        this(errorMessage, null);
    }

    /**
     * @param ex Cause
     */
    public JaxbRuntimeException(Exception ex) {
        this("", ex);
    }

    /**
     * @param errorMessage Error message
     * @param ex           Cause
     */
    public JaxbRuntimeException(String errorMessage, Exception ex) {
        super(errorMessage, ex);
    }


}
