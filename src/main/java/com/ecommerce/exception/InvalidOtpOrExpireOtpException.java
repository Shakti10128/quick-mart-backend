package com.ecommerce.exception;

public class InvalidOtpOrExpireOtpException extends RuntimeException{
    public InvalidOtpOrExpireOtpException(String message){
        super(message);
    }
}
