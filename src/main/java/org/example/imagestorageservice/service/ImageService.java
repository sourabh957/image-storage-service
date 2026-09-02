package org.example.imagestorageservice.service;

import lombok.RequiredArgsConstructor;
import org.example.imagestorageservice.dto.CreateImageRequest;
import org.example.imagestorageservice.dto.ImageResponse;
import org.example.imagestorageservice.entities.Image;
import org.example.imagestorageservice.exception.ImageNotFoundException;
import org.example.imagestorageservice.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public ImageResponse createImage(CreateImageRequest request, UUID imageId) {
        Image image = new Image();
        image.setId(imageId);
        image.setOriginalFileName(request.originalFileName());
        image.setContentType(request.contentType());
        image.setFileSize(request.fileSize());
        image.setS3Key(request.s3Key());
        image.setCreatedAt(Instant.now());

        Image savedImage = imageRepository.save(image);
        return new ImageResponse(
                savedImage.getId(),
                savedImage.getOriginalFileName(),
                savedImage.getContentType(),
                savedImage.getFileSize(),
                savedImage.getS3Key(),
                savedImage.getCreatedAt()
        );
    }

    public ImageResponse getImage(UUID imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException(imageId));

        return new ImageResponse(
                image.getId(),
                image.getOriginalFileName(),
                image.getContentType(),
                image.getFileSize(),
                image.getS3Key(),
                image.getCreatedAt()
        );
    }
}
