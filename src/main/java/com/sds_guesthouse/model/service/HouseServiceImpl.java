package com.sds_guesthouse.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sds_guesthouse.exception.ExplicitMessageException;
import com.sds_guesthouse.model.dao.HouseMapper;
import com.sds_guesthouse.model.dto.house.HouseRequestDto;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.HouseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseMapper houseMapper;

    @Override
    @Transactional
    public void createHouse(HouseRequestDto dto) {

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
    
    @Override
	public House getHouseDetail(Long houseId) {
	
	    House house = houseMapper.findById(houseId);
	
	    if (house == null) {
	        throw new IllegalArgumentException("해당 숙소가 존재하지 않습니다.");
	    }
	
	    return house;
	}
    
    @Override
    @Transactional
    public void updateHouse(Long houseId, HouseRequestDto dto) {
        // 1. 기존 숙소 정보 조회
        House house = houseMapper.findById(houseId);
        if (house == null) {
            throw new IllegalArgumentException("해당 숙소가 존재하지 않습니다.");
        }
        // 2. 수정된 데이터 반영
        house.setName(dto.getName());
        house.setAddress(dto.getAddress());
        house.setPrice(dto.getPrice());
        house.setMaxGuests(dto.getMaxGuests());
        house.setDescription(dto.getDescription());
     
        // 3. DB에 업데이트
        int result = houseMapper.updateHouse(house);
        if (result == 0) {
            throw new ExplicitMessageException("숙소 정보 수정에 실패했습니다.");
        }
    }
}