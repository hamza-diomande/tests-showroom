package com.oceane.dm.auth.model.exception;

public class SecretNotSetException extends RuntimeException {
    public SecretNotSetException(String message){
        super(message);
    }
}
