package org.example.imagestorageservice.service;

import org.example.imagestorageservice.dto.CreateImageRequest;
import org.example.imagestorageservice.dto.ImageResponse;
import org.example.imagestorageservice.entities.Image;
import org.example.imagestorageservice.repository.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    private UUID imageId;
    private CreateImageRequest request;

    @BeforeEach
    void setUp() {
        imageId = UUID.randomUUID();
        request = new CreateImageRequest(
            "test-image.jpg",
            "image/jpeg",
            1024L,
            "s3-key-12345"
        );
    }

    @Test
    void shouldCreateImageSuccessfully() {
        Image image = new Image();
        image.setId(imageId);
        image.setOriginalFileName(request.getOriginalFileName());
        image.setContentType(request.getContentType());
        image.setFileSize(request.getFileSize());
        image.setS3Key(request.getS3Key());

        when(imageRepository.save(any(Image.class))).thenReturn(image);

        ImageResponse response = imageService.createImage(request, imageId);

        assertEquals(imageId, response.getId());
        assertEquals(request.getOriginalFileName(), response.getOriginalFileName());
        assertEquals(request.getContentType(), response.getContentType());
        assertEquals(request.getFileSize(), response.getFileSize());
        assertEquals(request.getS3Key(), response.getS3Key());
    }

}
