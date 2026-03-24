package com.sds_guesthouse.model.service;

import com.sds_guesthouse.model.dao.HouseImageMapper;
import com.sds_guesthouse.model.entity.HouseImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public interface HouseImageService {

    // 이미지 순서 자동 생성
    public int getNextImageOrder(Long houseId);

    // 이미지 파일을 저장하는 메서드
    public void saveImageFile(MultipartFile imageFile, String imagePath) throws IOException;

    // 이미지 업로드 및 DB 저장
    public void uploadHouseImage(Long houseId, MultipartFile imageFile) throws IOException;
}