package com.sbolo.syk.common.exception;

@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {
    private Integer code;

    @Override
    public String getMessage() {
    	return super.getMessage();
    }

    public BusinessException() {
        super();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(Integer code, String message) {
    	super(message);
    	if(code != null){
    		this.code = code;
    	}else{
    		this.code = 10001;
    	}
    	
    }
    public BusinessException(int code) {
    	super(code+"");
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
    
    public Integer getCode(){
    	return code;
    }
}
