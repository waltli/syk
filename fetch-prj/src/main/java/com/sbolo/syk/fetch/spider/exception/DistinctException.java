package com.sbolo.syk.fetch.spider.exception;

public class DistinctException extends Exception {
	private static final long serialVersionUID = 1L;
	
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public DistinctException() {
        super();
    }

    public DistinctException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistinctException(String message) {
        super(message);
    }

    public DistinctException(Throwable cause) {
        super(cause);
    }
}
