package com.ecommerce.exception.category;

public class CategoryAlreadyExistException extends RuntimeException{
    public CategoryAlreadyExistException(String message){
        super(message);
    }
}
