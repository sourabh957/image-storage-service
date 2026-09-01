package org.example.imagestorageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ImageResponse {

    private UUID id;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String s3Key;
    private Instant createdAt;
}
