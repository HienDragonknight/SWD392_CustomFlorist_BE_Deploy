package edu.fpt.customflorist.exceptions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PhoneNumberExistsException.class)
    protected ResponseEntity<Object> handlePhoneNumberExistsException(
            PhoneNumberExistsException ex, WebRequest request) {

        String bodyOfResponse = ex.getMessage();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Error-Message", bodyOfResponse);

        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(UserException.class)
    protected ResponseEntity<Object> handleCustomException(
            UserException ex, WebRequest request) {

        String bodyOfResponse = ex.getMessage();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Error-Message", bodyOfResponse);

        return handleExceptionInternal(ex, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }
}
