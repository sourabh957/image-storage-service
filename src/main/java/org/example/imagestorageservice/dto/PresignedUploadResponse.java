package org.example.imagestorageservice.dto;

public record PresignedUploadResponse(
        String imageId,
        String key,
        String uploadUrl
) {
}
