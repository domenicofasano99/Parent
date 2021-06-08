package com.bok.parent.be.controller;

import com.bok.parent.be.exception.AccountException;
import com.bok.parent.be.exception.ApiError;
import com.bok.parent.be.exception.EmailAlreadyExistsException;
import com.bok.parent.be.exception.InvalidCredentialsException;
import com.bok.parent.be.exception.TokenAuthenticationException;
import com.bok.parent.be.exception.TokenNotFoundException;
import com.fasterxml.jackson.core.JsonParseException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import reactor.util.annotation.NonNull;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected @NonNull
    ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }


    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    protected ResponseEntity<Object> emailAlreadyRegistered(EmailAlreadyExistsException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(TokenAuthenticationException.class)
    protected ResponseEntity<Object> badTokenHandler(TokenAuthenticationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    protected ResponseEntity<Object> badCredentialsHandler(InvalidCredentialsException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllUnhandled(Exception ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(AccountException.class)
    protected ResponseEntity<Object> handleAccountException(AccountException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    protected ResponseEntity<Object> handleTokenNotFoundException(TokenNotFoundException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleAllUnhandledRuntimeExceptions(RuntimeException ex) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}