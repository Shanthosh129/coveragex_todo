package com.backend.app.exception;

import java.util.List;
import com.backend.app.dto.ErrorMsg;
import lombok.Getter;

@Getter
public class ProcessingException extends RuntimeException {
    private final List<ErrorMsg> errors;
    private final String requestId;

    public ProcessingException(List<ErrorMsg> errors, String requestId) {
        super("Processing failed with errors");
        this.errors = errors;
        this.requestId = requestId;
    }

    public List<ErrorMsg> getErrorMsgs() {
        return errors;
    }
}