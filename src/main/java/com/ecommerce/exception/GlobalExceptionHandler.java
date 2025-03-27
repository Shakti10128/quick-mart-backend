package com.ecommerce.exception;

import com.ecommerce.exception.category.CategoryAlreadyExistException;
import com.ecommerce.exception.category.CategoryNotExistException;
import com.ecommerce.exception.product.ProductNotFoundException;
import com.ecommerce.exception.user.UserAlreadyExistException;
import com.ecommerce.exception.user.UserNotFoundException;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ****************************** Validation errors *******************************
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage()) // Collect all error messages
                .collect(Collectors.joining(", ")); // Join multiple messages

        Map<String, String> response = new HashMap<>();
        response.put("success", "false");
        response.put("message", errorMessage); // Return a single message field

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> accessDeniedExceptionHandler(AccessDeniedException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                false,"Access is denied", request.getRequestURI(), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorDetails> userAlreadyExistException(UserAlreadyExistException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                false,e.getMessage(), request.getRequestURI(), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDetails> userNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                false,e.getMessage(), request.getRequestURI(), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOtpOrExpireOtpException.class)
    public ResponseEntity<ErrorDetails> invalidOtpException(InvalidOtpOrExpireOtpException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                false,e.getMessage(), request.getRequestURI(), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ErrorDetails> imageUploadException(ImageUploadException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                false,e.getMessage(), request.getRequestURI(), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // ****************************** Category Exception handling ************************

    @ExceptionHandler(CategoryAlreadyExistException.class)
    public ResponseEntity<ErrorDetails> categoryAlreadyExistException(CategoryAlreadyExistException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                false,e.getMessage(), request.getRequestURI(), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryNotExistException.class)
    public ResponseEntity<ErrorDetails> categoryNotExistException(CategoryNotExistException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                false,e.getMessage(), request.getRequestURI(), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    // **************************** product exceptions *******************************
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDetails> productNotFoundException(ProductNotFoundException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                false,e.getMessage(), request.getRequestURI(), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    // ************************** Address Exceptions **********************************
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorDetails> addressNotFoundException(AddressNotFoundException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                false,e.getMessage(), request.getRequestURI(), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }


    // ****************************** Razorpay Exceptions *****************************
    @ExceptionHandler(InvalidCreationRazorpayException.class)
    public ResponseEntity<ErrorDetails> invalidCreationRazorpayException(InvalidCreationRazorpayException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                false,e.getMessage(), request.getRequestURI(), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
