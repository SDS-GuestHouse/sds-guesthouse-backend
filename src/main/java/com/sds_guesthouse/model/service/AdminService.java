package com.sds_guesthouse.model.service;

import com.sds_guesthouse.model.dto.host.HostSigninRequestDto;
import com.sds_guesthouse.model.entity.Admin;

public interface AdminService {
	
    /**
     * 관리자 로그인 로직
     * @param dto 로그인 시도 정보 (userId, password)
     * @return 로그인 성공 시 조회된 호스트 엔티티 객체
     * @throws IllegalArgumentException 보안상 아이디/비밀번호 불일치를 통합하여 예외 처리할 때 -> "입력 정보가 올바르지 않거나 처리할 수 없는 요청입니다."
     */
	Admin login(HostSigninRequestDto dto);

}
