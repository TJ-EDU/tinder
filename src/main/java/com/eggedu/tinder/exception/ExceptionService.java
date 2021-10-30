package com.eggedu.tinder.exception;

public class ExceptionService extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ExceptionService() {
		
	}
	public ExceptionService(String msn) {
		super(msn);
	}
}
