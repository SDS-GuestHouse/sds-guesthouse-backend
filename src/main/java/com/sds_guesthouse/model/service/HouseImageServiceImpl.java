package com.sds_guesthouse.model.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sds_guesthouse.model.dao.HouseImageMapper;
import com.sds_guesthouse.model.entity.HouseImage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HouseImageServiceImpl implements HouseImageService{

    private final HouseImageMapper houseImageMapper;

    // 이미지 순서 자동 생성
    public int getNextImageOrder(Long houseId) {
        Integer maxOrder = houseImageMapper.findMaxOrderByHouseId(houseId);
        return (maxOrder == null) ? 0 : maxOrder + 1;
    }

    // 이미지 파일을 저장하는 메서드
    public void saveImageFile(MultipartFile imageFile, String imagePath) throws IOException {
        Path path = Paths.get("uploads/house_images", imagePath);
        Files.createDirectories(path.getParent());  // 디렉토리가 없으면 생성
        imageFile.transferTo(path);  // 실제 파일을 지정한 경로에 저장
    }

    // 이미지 업로드 및 DB 저장
    public void uploadHouseImage(Long houseId, MultipartFile imageFile) throws IOException {

    	int display_order = getNextImageOrder(houseId); // 다음 이미지 순서 계산

        // UUID로 이미지 파일 이름 생성
        String extension = "jpg";
        String imagePath = UUID.randomUUID().toString() + "." + extension;  // UUID로 파일 이름 생성
        
        // 이미지 파일 저장
        saveImageFile(imageFile, imagePath);

        // HouseImage 엔티티 생성
        HouseImage houseImage = new HouseImage(null, houseId, imagePath, display_order, null, null);
        // MyBatis를 이용해 DB에 이미지 정보 삽입
        houseImageMapper.insertHouseImage(houseImage);
    }
}