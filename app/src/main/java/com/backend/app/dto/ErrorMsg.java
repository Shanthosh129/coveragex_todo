package com.backend.app.dto;

public record ErrorMsg(
        int code,
        String message
) {}
