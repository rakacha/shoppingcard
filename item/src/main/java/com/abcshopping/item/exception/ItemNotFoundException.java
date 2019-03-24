package com.abcshopping.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Item not found!")
public class ItemNotFoundException extends RuntimeException {
	public ItemNotFoundException(String id) {
		super(id);
	}
		
	@Override
	public synchronized Throwable fillInStackTrace() {
		// TODO Auto-generated method stub
		return this;
	}
	
}
