package com.backend.app.dto;

import java.util.List;

public record ErrorResponse(
        List<ErrorMsg> errors,
        String requestId
) {}
