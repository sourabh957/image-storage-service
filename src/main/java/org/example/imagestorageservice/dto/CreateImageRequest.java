package org.example.imagestorageservice.dto;

public record CreateImageRequest(
        String originalFileName,
        String contentType,
        Long fileSize,
        String s3Key
) {
}
