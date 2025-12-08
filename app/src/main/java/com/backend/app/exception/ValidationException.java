package com.backend.app.exception;

import java.util.List;
import com.backend.app.dto.ErrorMsg;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final String requestId;
    private final List<ErrorMsg> errorMsgs;

    public ValidationException(List<ErrorMsg> errorMsgs, String requestId) {
        super("Validation failed with errors");
        this.errorMsgs = errorMsgs;
        this.requestId = requestId;
    }

    public List<ErrorMsg> getErrors() {
        return errorMsgs;
    }
}
