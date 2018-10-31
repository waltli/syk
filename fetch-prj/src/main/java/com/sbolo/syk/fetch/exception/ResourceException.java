package com.sbolo.syk.fetch.exception;

@SuppressWarnings("serial")
public class ResourceException extends Exception {
    private Integer code;

    @Override
    public String getMessage() {
    	return super.getMessage();
    }

    public ResourceException() {
        super();
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceException(String message) {
        super(message);
    }
    
    public ResourceException(Integer code, String message) {
    	super(message);
    	if(code != null){
    		this.code = code;
    	}else{
    		this.code = 10001;
    	}
    	
    }
    public ResourceException(int code) {
    	super(code+"");
    }

    public ResourceException(Throwable cause) {
        super(cause);
    }
    
    public Integer getCode(){
    	return code;
    }
}
