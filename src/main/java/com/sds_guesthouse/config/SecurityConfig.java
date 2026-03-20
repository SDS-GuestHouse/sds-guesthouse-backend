package com.sds_guesthouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
        	// 1. CSRF 보안 비활성화 (테스트 편의를 위해, 실무에선 설정 필요)
        	.csrf(csrf -> csrf.disable())
        	
        	// // 2. URL별 권한 제어 (가장 중요한 부분!)
            // .authorizeHttpRequests(auth -> auth
            //     // 호스트 전용 API는 ROLE_HOST 권한이 있어야만 접근 가능
            //     .requestMatchers("/api/v1/host/**").hasRole("HOST")
            //     // 로그인, 회원가입 등 공통 API는 누구나 접근 가능
            //     .requestMatchers("/api/v1/auth/**", "/signin", "/signup").permitAll()
            //     // 그 외 모든 요청은 인증(로그인)이 필요함
            //     .anyRequest().authenticated()
            // )
            
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 모든 요청을 인증 없이 허용 (로그인 상태 아니어도 인증되도록)
                // 추후 회원가입, 로그인 등만 permitAll()로 처리해줘야함
                // 나머지는 **authenticated()**로 설정해서 로그인한 사람만 쓰게 바꿉니다.
            )
            
         // 3. 세션 관리 전략
            .sessionManagement(session -> session
                // 세션이 없으면 새로 생성하고, 있으면 기존 것 사용 (IF_REQUIRED)
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );
        return http.build();

    }


}	