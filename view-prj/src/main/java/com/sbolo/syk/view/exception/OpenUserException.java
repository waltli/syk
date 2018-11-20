package com.sbolo.syk.view.exception;

@SuppressWarnings("serial")
public class OpenUserException extends RuntimeException {
    private Integer code;

    @Override
    public String getMessage() {
    	return super.getMessage();
    }

    public OpenUserException() {
        super();
    }

    public OpenUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenUserException(String message) {
        super(message);
    }
    
    public OpenUserException(Integer code, String message) {
    	super(message);
    	if(code != null){
    		this.code = code;
    	}else{
    		this.code = 10001;
    	}
    	
    }
    public OpenUserException(int code) {
    	super(code+"");
    }

    public OpenUserException(Throwable cause) {
        super(cause);
    }
    
    public Integer getCode(){
    	return code;
    }
}
