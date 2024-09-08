package com.oceane.dm.feedback.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * Classe d'exception utilisée lors d'une erreur durant la validation d'un captcha.
 */
public class CaptchaFailedException extends AuthenticationException {
    /**
     * Création d'une exception.
     * @param msg   le message
     * @param cause la cause
     */
    public CaptchaFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Création d'une exception.
     * @param msg le message
     */
    public CaptchaFailedException(String msg) {
        super(msg);
    }
}
