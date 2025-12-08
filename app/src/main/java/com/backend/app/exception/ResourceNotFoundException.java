package com.backend.app.exception;

import java.util.List;
import com.backend.app.dto.ErrorMsg;
import lombok.Getter;
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String requestId;
    private final List<ErrorMsg> errorMsgs;
    public ResourceNotFoundException(List<ErrorMsg> errorMsgs, String requestId) {
        super("Resource Not Found");
         this.errorMsgs = errorMsgs;
        this.requestId = requestId;
    }
     public List<ErrorMsg> getErrors() {
        return errorMsgs;
    }
}
