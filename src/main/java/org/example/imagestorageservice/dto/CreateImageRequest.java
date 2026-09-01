package org.example.imagestorageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateImageRequest {
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String s3Key;
}
