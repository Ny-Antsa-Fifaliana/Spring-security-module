package com.module.security.exception;


public class ResponseMessage {
	private boolean success;
	private String message;
	private Object body;
	
	public ResponseMessage(boolean success, String message, Object body) {
		this.success=success;
		this.message=message;
		this.body=body;
	}

	public ResponseMessage(boolean success, String message) {
		this.success=success;
		this.message=message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
	
	
}
