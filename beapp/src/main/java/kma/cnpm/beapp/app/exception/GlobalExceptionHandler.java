package kma.cnpm.beapp.app.exception;

import jakarta.validation.ConstraintViolationException;
import kma.cnpm.beapp.domain.common.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    Handle exception validation
    @ExceptionHandler({MethodArgumentNotValidException.class , ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseMultipleField handleValidationException(Exception exception , WebRequest webRequest , MethodArgumentNotValidException methodArgumentNotValidException){
        methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
        Map<String, String> mess = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            mess.put(fieldName, errorMessage);
        });
        ErrorResponseMultipleField errorResponse = new ErrorResponseMultipleField();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(webRequest.getDescription(false).replace("uri=" , ""));
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setMessage(mess);
        return errorResponse;

    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAppException(AppException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setStatus(e.getErrorCode().getCode());
        errorResponse.setError(AppException.class.getSimpleName());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }



}
