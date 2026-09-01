package org.example.imagestorageservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.imagestorageservice.dto.CreateImageRequest;
import org.example.imagestorageservice.dto.ImageResponse;
import org.example.imagestorageservice.entities.PresignedUploadRequest;
import org.example.imagestorageservice.entities.PresignedUploadResponse;
import org.example.imagestorageservice.service.ImageService;
import org.example.imagestorageservice.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;
    private final S3Service s3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        UUID imageId = UUID.randomUUID();
        String key = "images/" + imageId + "-" + file.getOriginalFilename();
        s3Service.upload(file, key);
        
        // Save metadata to database
        CreateImageRequest request = new CreateImageRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                key
        );
        ImageResponse response = imageService.createImage(request, imageId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ImageResponse getImage(@PathVariable("id") UUID imageId) {
        return imageService.getImage(imageId);
    }

    @PostMapping("/presigned-url")
    @ResponseStatus(HttpStatus.CREATED)
    public PresignedUploadResponse generateUploadUrl(
            @RequestBody PresignedUploadRequest request
    ) {
        String imageId = UUID.randomUUID().toString();
        String fileName = request.filename();
        String key = "images/" + imageId + "-" + fileName;
        log.info("Generating presigned URL for key {} with content type {}", key, request.contentType());

        String presignedUrl = s3Service.generatePresignedUrl(key, request.contentType());
        log.info("Generated presigned URL for key {}: {}", key, presignedUrl);

        return new PresignedUploadResponse(imageId, key, presignedUrl);
    }

}
