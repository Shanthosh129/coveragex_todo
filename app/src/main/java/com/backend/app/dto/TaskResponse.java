package com.backend.app.dto;

import java.time.LocalDateTime;

public record TaskResponse(
    Long id,
    String title,
    String description,
    LocalDateTime createdAt
) {}
