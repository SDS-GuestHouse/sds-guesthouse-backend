import com.sds_guesthouse.exception.ExplicitMessageException;
import com.sds_guesthouse.model.dto.HostSignupRequestDto;
import com.sds_guesthouse.model.mapper.HostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HostServiceImpl implements HostService {

    private final HostMapper hostMapper;

    @Override
    @Transactional
    public void registerHost(HostSignupRequestDto dto) {
        
        // 1. 아이디 중복 체크 (보안 강화 영역)
        // 중복 시 IllegalArgumentException을 던져서 "입력 정보가 올바르지 않습니다"로 응답 통일
        if (hostMapper.existsByUserId(dto.getUserId())) {
            throw new IllegalArgumentException("Duplicate ID: " + dto.getUserId());
        }

        // 2. 서비스 정책 체크 (명시적 안내 영역 - 예시)
        // 만약 특정 블랙리스트 전화번호나 정책상 가입 불가 조건이 있다면?
        if (dto.getPhone().contains("000-0000")) {
            throw new ExplicitMessageException("해당 연락처로는 가입이 불가능합니다. 고객센터에 문의하세요.");
        }

        // 3. 비밀번호 암호화 (추후 Security 설정 후 적용)
        // String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 4. DB 저장 (MyBatis 호출)
        int result = hostMapper.insertHost(dto);
        
        if (result == 0) {
            throw new RuntimeException("회원가입 중 서버 오류가 발생했습니다.");
        }
    }
}