package com.sds_guesthouse.model.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sds_guesthouse.exception.ExplicitMessageException;
import com.sds_guesthouse.model.dao.HouseMapper;
import com.sds_guesthouse.model.dto.house.HouseRequestDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.HouseStatus;
import com.sds_guesthouse.model.entity.Reservation;
import com.sds_guesthouse.util.auth.SessionUserProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseMapper houseMapper;
    private final HouseImageService houseImageService;
    private final SessionUserProvider sessionUserProvider;

    @Override
    @Transactional
    public void createHouse(HouseRequestDto dto) {
        House house = House.builder()
                .hostId(sessionUserProvider.getCurrentUserId())
                .name(dto.getName())
                .address(dto.getAddress())
                .price(dto.getPrice())
                .maxGuests(dto.getMaxGuests())
                .description(dto.getDescription())
                .status(HouseStatus.CREATE_PENDING)
                .build();

        if (houseMapper.insertHouse(house) == 0) {
            throw new ExplicitMessageException("Failed to create house.");
        }
    }

    @Override
    public House getHouseDetail(Long houseId) {
        House house = houseMapper.findById(houseId);
        if (house == null) {
            throw new ExplicitMessageException("House not found.");
        }
        return house;
    }

    @Override
    public List<House> getMyHouses() {
        return houseMapper.findByHostId(sessionUserProvider.getCurrentUserId());
    }

    @Override
    @Transactional
    public void updateHouse(Long houseId, HouseRequestDto dto) {
        House house = requireOwnedHouse(houseId);
        house.setName(dto.getName());
        house.setAddress(dto.getAddress());
        house.setPrice(dto.getPrice());
        house.setMaxGuests(dto.getMaxGuests());
        house.setDescription(dto.getDescription());

        if (houseMapper.updateHouse(house) == 0) {
            throw new ExplicitMessageException("Failed to update house.");
        }
    }

    @Override
    @Transactional
    public void deleteHouse(Long houseId) {
        House house = requireOwnedHouse(houseId);
        house.setStatus(HouseStatus.DELETE_PENDING);

        if (houseMapper.updateHouse(house) == 0) {
            throw new ExplicitMessageException("Failed to request house deletion.");
        }
    }

    @Override
    public List<Reservation> getReservationsByHouseId(Long houseId) {
        requireOwnedHouse(houseId);
        return houseMapper.findReservationsByHouseId(houseId);
    }

    @Override
    public List<House> getAvailableHouses(LocalDate startDate, LocalDate endDate, String location, Integer numberOfGuests) {
        return houseMapper.findAvailableHouses(startDate, endDate, location, numberOfGuests);
    }

    @Override
    public void uploadHouseImage(Long houseId, MultipartFile imageFile) {
        requireOwnedHouse(houseId);
        try {
            houseImageService.uploadHouseImage(houseId, imageFile);
        } catch (IOException e) {
            throw new ExplicitMessageException("Failed to upload house image.");
        }
    }

    private House requireOwnedHouse(Long houseId) {
        House house = houseMapper.findById(houseId);
        if (house == null) {
            throw new ExplicitMessageException("House not found.");
        }
        if (!house.getHostId().equals(sessionUserProvider.getCurrentUserId())) {
            throw new AccessDeniedException("You do not own this house.");
        }
        return house;
    }
}
