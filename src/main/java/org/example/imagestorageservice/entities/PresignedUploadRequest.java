package org.example.imagestorageservice.entities;

public record PresignedUploadRequest(
        String filename,
        String contentType
) {
}
