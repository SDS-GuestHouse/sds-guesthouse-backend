package com.sds_guesthouse.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;


@RestController
@RequestMapping("/api/v1/house_image/")
public class HouseImageController {

    private static final String UPLOAD_DIR = "uploads/house_images/";  // 업로드된 파일이 저장된 디렉토리

    // 파일 제공 엔드포인트
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
    	try {
            // 파일 경로를 절대 경로로 가져옴
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();

            // 파일을 Resource 객체로 변환
            Resource resource = new UrlResource(filePath.toUri());

            // 파일이 존재하고 읽을 수 있으면 리턴
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
                } else {
                    return ResponseEntity.status(400).body(null);
            }
        } catch (IOException e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}