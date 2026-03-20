package com.sds_guesthouse.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sds_guesthouse.exception.ExplicitMessageException;
import com.sds_guesthouse.model.dao.HouseMapper;
import com.sds_guesthouse.model.dto.HouseCreateRequestDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.HouseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseMapper houseMapper;

    @Override
    @Transactional
    public void createHouse(HouseCreateRequestDto dto) {

        // TODO: 나중에 로그인 붙이면 실제 hostId 가져오기
        Long hostId = getCurrentHostId();

        House house = House.builder()
                .hostId(hostId)
                .name(dto.getName())
                .address(dto.getAddress())
                .price(dto.getPrice())
                .maxGuests(dto.getMaxGuests())
                .description(dto.getDescription())
                .status(HouseStatus.CREATE_PENDING) // 관리자 승인 대기
                .build();

        int result = houseMapper.insertHouse(house);

        if (result == 0) {
            throw new ExplicitMessageException("숙소 등록에 실패했습니다.");
        }
    }

    /**
     * 임시 hostId (테스트용)
     */
    private Long getCurrentHostId() {
        return 1L;
    }
}