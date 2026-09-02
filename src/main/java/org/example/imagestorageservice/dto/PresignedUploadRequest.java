package org.example.imagestorageservice.dto;

import jakarta.validation.constraints.NotBlank;

public record PresignedUploadRequest(
        @NotBlank String filename,
        @NotBlank String contentType
) {
}
