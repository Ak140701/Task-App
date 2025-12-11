package com.aks.tasks.domain.dto;

public record ErrorResponse(
        int status,
        String messsage,
        String details
) {
}
