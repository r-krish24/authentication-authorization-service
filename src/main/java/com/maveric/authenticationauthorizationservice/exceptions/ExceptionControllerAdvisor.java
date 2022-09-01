package com.maveric.authenticationauthorizationservice.exceptions;

import com.maveric.authenticationauthorizationservice.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.maveric.authenticationauthorizationservice.constants.Constants.*;

@RestControllerAdvice
public class ExceptionControllerAdvisor {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ErrorDto errorDto = new ErrorDto();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        errorDto.setCode(BAD_REQUEST_CODE);
        errorDto.setMessage(BAD_REQUEST_MESSAGE);
        errorDto.setErrors(errors);
        return errorDto;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorDto handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(METHOD_NOT_ALLOWED_CODE);
        errorDto.setMessage(METHOD_NOT_ALLOWED_MESSAGE);
        return errorDto;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDto handleHttpRequestInvalidCredentialsException(
            InvalidCredentialsException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(NOT_AUTHORIZED_CODE);
        errorDto.setMessage(NOT_AUTHORIZED_MESSAGE);
        return errorDto;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleHttpRequestUserNotFoundException(
            UserNotFoundException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(USER_NOT_FOUND_CODE);
        errorDto.setMessage(USER_NOT_FOUND_MESSAGE);
        return errorDto;
    }

    @ExceptionHandler(AccountCreationFailedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorDto handleHttpRequestAccountCreationFailedException(
            AccountCreationFailedException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setCode(ACCOUNT_CREATION_FAILED_CODE);
        errorDto.setMessage(ACCOUNT_CREATION_FAILED_MESSAGE);
        return errorDto;
    }




}