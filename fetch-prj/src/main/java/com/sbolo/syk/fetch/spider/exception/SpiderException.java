package com.sbolo.syk.fetch.spider.exception;

public class SpiderException extends Exception {
	private static final long serialVersionUID = 1L;
	
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public SpiderException() {
        super();
    }

    public SpiderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpiderException(String message) {
        super(message);
    }

    public SpiderException(Throwable cause) {
        super(cause);
    }
}
