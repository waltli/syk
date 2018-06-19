package com.sbolo.syk.common.exception;

import org.slf4j.Logger;

public class AnalystException extends Exception {
    private static final long serialVersionUID = 775477636706597359L;

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public AnalystException() {
        super();
    }

    public AnalystException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnalystException(String message) {
        super(message);
    }

    public AnalystException(Throwable cause) {
        super(cause);
    }
}
