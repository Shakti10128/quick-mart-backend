package com.ecommerce.exception.category;

public class CategoryNotExistException extends RuntimeException{
    public CategoryNotExistException(String message){
        super(message);
    }
}
