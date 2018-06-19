package com.sbolo.syk.common.exception;

public class MovieInfoFetchException extends Exception {
	private static final long serialVersionUID = 1L;
	
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public MovieInfoFetchException() {
        super();
    }

    public MovieInfoFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public MovieInfoFetchException(String message) {
        super(message);
    }

    public MovieInfoFetchException(Throwable cause) {
        super(cause);
    }
}
