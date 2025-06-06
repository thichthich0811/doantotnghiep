package com.web.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.web.utils.ErrorResponse;
import com.web.utils.FieldErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public static ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        List<FieldErrorInfo> errorInfos = new ArrayList<>();
        for (FieldError error : errors) {
            String field = error.getField();
            String message = error.getDefaultMessage();
            FieldErrorInfo errorInfo = new FieldErrorInfo();
            errorInfo.setField(field);
            errorInfo.setMessage(message);
            errorInfos.add(errorInfo);
        }
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(Instant.now().toString());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setErrors(errorInfos);
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

}

