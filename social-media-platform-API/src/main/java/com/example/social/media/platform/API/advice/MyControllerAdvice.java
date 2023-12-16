package com.example.social.media.platform.API.advice;


import com.example.social.media.platform.API.exception.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class MyControllerAdvice extends ResponseEntityExceptionHandler {


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenAccess(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Access denied"));
    }


    @ExceptionHandler(NoSuchElementException.class)  // inbuilt exception
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException exception){
        return new ResponseEntity<>("No value is present in db , please change yore request ", HttpStatus.NOT_FOUND);
    }

    //    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);

        return new ResponseEntity<Object>("please change http method type", HttpStatus.NOT_FOUND);
    }

}
