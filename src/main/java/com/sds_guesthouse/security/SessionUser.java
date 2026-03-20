package com.sds_guesthouse.security;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 세션에 저장할 인증 사용자 정보 DTO
 * 보안과 성능을 위해 엔티티 대신 필요한 정보(ID, 이름, 권한)만 담습니다.
 */
@Getter
@Builder
@NoArgsConstructor // 시큐리티나 직렬화 과정에서 기본 생성자가 필요할 수 있음
@AllArgsConstructor
public class SessionUser implements Serializable {

    // 자바 직렬화 버전 관리를 위한 ID (생략 가능하지만 추가하면 안정적임)
	// 세션은 서버 재시작이나 클러스터링(서버 확장) 환경에서 객체를 '데이터 덩어리'로 변환해야 할 때가 있습니다. 이때 이 인터페이스가 없으면 에러가 발생합니다.
    private static final long serialVersionUID = 1L;

    private Long id;        // 데이터베이스 PK (HostId, GuestId 등)
    private String name;    // 화면 표시용 이름 (홍길동님 환영합니다)
    private String role;    // 권한 식별자 (ROLE_HOST, ROLE_GUEST, ROLE_ADMIN)

    /**
     * 권한 체크 시 "ROLE_" 접두사 유무를 확인하기 위한 편의 메서드
     * Spring Security는 기본적으로 권한을 확인할 때 이름 앞에 **ROLE_**이라는 글자가 붙어 있는지를 체크합니다.
	 * Spring Security의 판단: * hasRole('ADMIN')이라고 코드를 짜면, 내부적으로는 ROLE_ADMIN이라는 글자가 있는지 찾습니다.
	 * 만약 내 변수(this.role)에 ROLE_이 안 붙어 있다면, 나중에 권한 체크 과정에서 오류가 나거나 권한이 없다고 판단될 수 있습니다.
     */
    public String getAuthority() {
        return this.role;
    }
}