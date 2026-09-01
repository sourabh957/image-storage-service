package org.example.imagestorageservice.entities;

public record PresignedUploadResponse(
        String imageId,
        String key,
        String uploadUrl
) {
}
