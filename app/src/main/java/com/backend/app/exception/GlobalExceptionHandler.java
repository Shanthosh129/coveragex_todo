package com.backend.app.exception;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.backend.app.dto.ErrorMsg;
import com.backend.app.dto.ErrorResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        log.error("ValidationException occurred: {}", ex.getMessage());
        List<ErrorMsg> errors = ex.getErrors();
        ErrorResponse errorResponse = new ErrorResponse(errors, ex.getRequestId());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("ResourceNotFoundException occurred: {}", ex.getMessage());
        List<ErrorMsg> errors = ex.getErrors();
        ErrorResponse errorResponse = new ErrorResponse(errors, ex.getRequestId());
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(ProcessingException.class)
    public ResponseEntity<ErrorResponse> handleProcessingException(ProcessingException ex) {
        List<ErrorMsg> errors = ex.getErrorMsgs();
        ErrorResponse errorResponse = new ErrorResponse(errors, ex.getRequestId()); // Use getter to set requestId
        return ResponseEntity.internalServerError().body(errorResponse);
    }

}
