package com.sds_guesthouse.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import com.sds_guesthouse.model.dao.HouseImageMapper;

@ExtendWith(MockitoExtension.class)
class HouseImageServiceImplTest {

    @Mock
    private HouseImageMapper houseImageMapper;

    @InjectMocks
    private HouseImageServiceImpl houseImageService;

    @Test
    void uploadHouseImage_deletesSavedFileWhenMapperInsertFails() throws Exception {
        Path uploadDir = Paths.get("uploads", "house_images");
        Files.createDirectories(uploadDir);
        Set<String> beforeFiles = listFileNames(uploadDir);

        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile",
                "house.jpg",
                "image/jpeg",
                new byte[] {1, 2, 3}
        );

        doThrow(new RuntimeException("db failure"))
                .when(houseImageMapper)
                .insertHouseImage(org.mockito.ArgumentMatchers.any());

        IOException thrown = assertThrows(
                IOException.class,
                () -> houseImageService.uploadHouseImage(1L, imageFile)
        );

        assertEquals("Failed to upload house image.", thrown.getMessage());
        verify(houseImageMapper).insertHouseImage(org.mockito.ArgumentMatchers.any());

        Set<String> afterFiles = listFileNames(uploadDir);
        if (!beforeFiles.equals(afterFiles)) {
            cleanupExtraFiles(uploadDir, beforeFiles, afterFiles);
        }
        assertEquals(beforeFiles, afterFiles);
    }

    private Set<String> listFileNames(Path uploadDir) throws IOException {
        try (Stream<Path> stream = Files.list(uploadDir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toSet());
        }
    }

    private void cleanupExtraFiles(Path uploadDir, Set<String> beforeFiles, Set<String> afterFiles) throws IOException {
        for (String fileName : afterFiles) {
            if (!beforeFiles.contains(fileName)) {
                Files.deleteIfExists(uploadDir.resolve(fileName));
            }
        }
    }
}
