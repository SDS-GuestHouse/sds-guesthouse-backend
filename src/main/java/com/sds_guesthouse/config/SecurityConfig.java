package com.sds_guesthouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 알고리즘을 사용하는 인코더를 빈으로 등록
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 테스트를 위해 CSRF 보호 잠시 해제 (CSRF 토큰 없이도 없이도 요청 가능하도록)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 모든 요청을 인증 없이 허용 (로그인 상태 아니어도 인증되도록)
                // 추후 회원가입, 로그인 등만 permitAll()로 처리해줘야함
                // 나머지는 **authenticated()**로 설정해서 로그인한 사람만 쓰게 바꿉니다.
            );
        return http.build();

    }


}	