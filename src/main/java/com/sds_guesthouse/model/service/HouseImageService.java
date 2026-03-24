package com.sds_guesthouse.model.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface HouseImageService {

    // 이미지 순서 자동 생성
    public int getNextImageOrder(Long houseId);

    // 이미지 파일을 저장하는 메서드
    public void saveImageFile(MultipartFile imageFile, String imagePath) throws IOException;

    // 이미지 업로드 및 DB 저장
    public void uploadHouseImage(Long hostId, Long houseId, MultipartFile imageFile);
}