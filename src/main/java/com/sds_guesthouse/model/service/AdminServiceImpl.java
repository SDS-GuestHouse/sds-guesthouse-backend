package com.sds_guesthouse.model.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sds_guesthouse.exception.BusinessException;
import com.sds_guesthouse.model.dao.AdminMapper;
import com.sds_guesthouse.model.dao.HostMapper;
import com.sds_guesthouse.model.dao.HouseMapper;
import com.sds_guesthouse.model.dto.host.HostSigninRequestDto;
import com.sds_guesthouse.model.entity.Admin;
import com.sds_guesthouse.model.entity.Host;
import com.sds_guesthouse.model.entity.House;
import com.sds_guesthouse.model.entity.HouseStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
    private final AdminMapper adminMapper;
    private final HouseMapper houseMapper;
    private final PasswordEncoder passwordEncoder; // 빈으로 등록한 BCrypt 주입
	
    @Override
    public Admin login(HostSigninRequestDto dto) {
    	Admin admin = adminMapper.findByUserId(dto.getUserId());
        // 존재하지 않거나 비밀번호 불일치 시 예외 발생
        if (admin == null || !passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new BusinessException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return admin;
    }
    
    @Override
    public List<House> getHousesByStatus(HouseStatus status) {
        return houseMapper.findByStatus(status);
    }

	@Override
	@Transactional
	public void updateHouseStatus(long houseId, HouseStatus status) {
		
		// 해당 숙소가 존재하는지 확인, 존재하지 않는다면 400 에러
		House house = houseMapper.findById(houseId);
        if (house == null) {
            throw new BusinessException("존재하지 않는 숙소 정보입니다.");
        }

        // 상태 업데이트 실행, 매퍼에 id와 변경할 status를 전달합니다.
        int result = houseMapper.updateStatus(houseId, status);
        
        // 만약 업데이트된 행이 0개라면
        if (result == 0) {
            throw new BusinessException("숙소 상태 변경에 실패했습니다.");
        }
		
	}

	

}
