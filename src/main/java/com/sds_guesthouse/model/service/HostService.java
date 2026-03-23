package com.sds_guesthouse.model.service;

import com.sds_guesthouse.model.dto.host.HostIdDuplicateCheckResponseDto;
import com.sds_guesthouse.model.dto.host.HostSigninRequestDto;
import com.sds_guesthouse.model.dto.host.HostSignupRequestDto;
import com.sds_guesthouse.model.entity.Host;

public interface HostService {
	
	// @throws ExplicitMessageException 사용자에게 상세 사유를 전달해야 할 때
	
    /**
     * 호스트 회원가입 로직
     * @param dto 가입 신청 정보
     * @return 
     * @throws IllegalArgumentException 보안상 상세 사유를 숨겨야 할 때 -> "입력 정보가 올바르지 않거나 처리할 수 없는 요청입니다."
     */
    void registerHost(HostSignupRequestDto dto);
    
    /**
     * 호스트 회원가입 중 아이디 중복 확인
     * @param dto 아이디 체크 정보 (userId)
     * @return 아이디 중복 체크 여부
     * @throws ExplicitMessageException  사용자에게 상세 사유를 전달해야 할 때 -> "이미 사용 중인 아이디입니다."
     */
	HostIdDuplicateCheckResponseDto isDuplicateId(String userId);
    
    /**
     * 호스트 로그인 로직
     * @param dto 로그인 시도 정보 (userId, password)
     * @return 로그인 성공 시 조회된 호스트 엔티티 객체
     * @throws IllegalArgumentException 보안상 아이디/비밀번호 불일치를 통합하여 예외 처리할 때 -> "입력 정보가 올바르지 않거나 처리할 수 없는 요청입니다."
     */
    Host login(HostSigninRequestDto dto);
    

    
}