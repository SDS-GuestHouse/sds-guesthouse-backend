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
public class HouseImageServiceImpl implements HouseImageService {

    private final HouseImageMapper houseImageMapper;

    @Override
    public int getNextImageOrder(Long houseId) {
        Integer maxOrder = houseImageMapper.findMaxOrderByHouseId(houseId);
        return (maxOrder == null) ? 0 : maxOrder + 1;
    }

    @Override
    public void saveImageFile(MultipartFile imageFile, String imagePath) throws IOException {
        Path path = Paths.get("uploads/house_images", imagePath);
        Files.createDirectories(path.getParent());
        imageFile.transferTo(path);
    }

    @Override
    public void uploadHouseImage(Long houseId, MultipartFile imageFile) throws IOException {
        int displayOrder = getNextImageOrder(houseId);
        String imagePath = UUID.randomUUID() + ".jpg";

        saveImageFile(imageFile, imagePath);

        HouseImage houseImage = new HouseImage(null, houseId, imagePath, displayOrder, null, null);
        houseImageMapper.insertHouseImage(houseImage);
    }
}
