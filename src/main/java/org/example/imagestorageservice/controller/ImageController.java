package org.example.imagestorageservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.imagestorageservice.dto.CreateImageRequest;
import org.example.imagestorageservice.dto.ImageResponse;
import org.example.imagestorageservice.dto.PresignedUploadRequest;
import org.example.imagestorageservice.dto.PresignedUploadResponse;
import org.example.imagestorageservice.service.ImageService;
import org.example.imagestorageservice.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        UUID imageId = UUID.randomUUID();
        String key = "images/" + imageId + "-" + file.getOriginalFilename();
        s3Service.upload(file, key);

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
    public PresignedUploadResponse generateUploadUrl(@Valid @RequestBody PresignedUploadRequest request) {
        String imageId = UUID.randomUUID().toString();
        String key = "images/" + imageId + "-" + request.filename();
        log.info("Generating presigned URL for key {} with content type {}", key, request.contentType());

        String presignedUrl = s3Service.generatePresignedUrl(key, request.contentType());
        return new PresignedUploadResponse(imageId, key, presignedUrl);
    }
}
