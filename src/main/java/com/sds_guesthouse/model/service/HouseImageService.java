package com.sds_guesthouse.model.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface HouseImageService {

    int getNextImageOrder(Long houseId);

    void saveImageFile(MultipartFile imageFile, String imagePath) throws IOException;

    void uploadHouseImage(Long houseId, MultipartFile imageFile) throws IOException;
}
