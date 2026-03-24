package com.sds_guesthouse.config;

import java.util.List;
import java.util.function.Supplier;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ADMIN_SIGNIN_URL = "/api/v1/admin/signin";
    private static final String ADMIN_LOGOUT_URL = "/api/v1/admin/logout";
    private static final String APP_LOGOUT_URL = "/api/v1/logout";

    private static final List<IpAddressMatcher> ADMIN_ALLOWED_IPS = List.of(
        new IpAddressMatcher("0.0.0.0/0") // NEED TO BE CHANGED!!
    );

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository
    ) throws Exception {

        applyCommon(http, securityContextRepository);

        http
            .securityMatcher("/api/v1/admin/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(ADMIN_SIGNIN_URL).access(this::adminIpOnly)
                .anyRequest().access(this::adminIpAndRole)
            )
            .logout(logout -> logout
                // /admin/logout 자체도 허용 IP에서만 처리되게 함
                .logoutRequestMatcher(request ->
                    isExactPath(request, ADMIN_LOGOUT_URL) && isAllowedAdminIp(request)
                )
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository
    ) throws Exception {

        applyCommon(http, securityContextRepository);

        http
            .authorizeHttpRequests(auth -> auth
        		.requestMatchers(
        	            "/api/v1/guest/signup", "/api/v1/guest/signin", "/api/v1/guest/check",
        	            "/api/v1/host/signup", "/api/v1/host/signin", "/api/v1/host/check",
        	            "/api/v1/logout"
        	        ).permitAll()
        	        .requestMatchers(HttpMethod.POST, "/api/v1/house/{id}/reserve").hasRole("GUEST")
        	        .requestMatchers(HttpMethod.GET, "/api/v1/house/my-house").hasRole("HOST")
        	        .requestMatchers(HttpMethod.POST, "/api/v1/house").hasRole("HOST")
        	        .requestMatchers(HttpMethod.POST, "/api/v1/house/{id}/image").hasRole("HOST")
        	        .requestMatchers(HttpMethod.PUT, "/api/v1/house/{id}").hasRole("HOST")
        	        .requestMatchers(HttpMethod.DELETE, "/api/v1/house/{id}").hasRole("HOST")
        	        .requestMatchers(HttpMethod.GET, "/api/v1/house/{id}/reservation").hasRole("HOST")
        	        .requestMatchers(HttpMethod.GET, "/api/v1/house", "/api/v1/house/{houseId}").permitAll()
        	        
        	        .requestMatchers(HttpMethod.GET, "/api/v1/reservation").hasRole("GUEST")
        	        .requestMatchers(HttpMethod.GET, "/api/v1/reservation/{id}").hasRole("GUEST")
        	        .requestMatchers(HttpMethod.DELETE, "/api/v1/reservation/{id}").hasRole("GUEST")
                .anyRequest().denyAll()
            		
//            		.anyRequest().permitAll()
            )
            .logout(logout -> logout
                .logoutUrl(APP_LOGOUT_URL)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            );

        return http.build();
    }

    private void applyCommon(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository
    ) throws Exception {
        http
        	.cors(cors -> {}) // CorsConfigurationSource 라는 이름의 Bean을 자동으로 찾아서 연결
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .securityContext(securityContext -> securityContext
                .requireExplicitSave(true)
                .securityContextRepository(securityContextRepository)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            );
    }

    private AuthorizationDecision adminIpOnly(
            Supplier<? extends Authentication> authentication,
            RequestAuthorizationContext context
    ) {
        return new AuthorizationDecision(isAllowedAdminIp(context.getRequest()));
    }

    private AuthorizationDecision adminIpAndRole(
            Supplier<? extends Authentication> authentication,
            RequestAuthorizationContext context
    ) {
        boolean allowedIp = isAllowedAdminIp(context.getRequest());

        Authentication auth = authentication.get();
        boolean isAdmin = auth != null
                && auth.isAuthenticated()
                && auth.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        return new AuthorizationDecision(allowedIp && isAdmin);
    }

    private boolean isAllowedAdminIp(HttpServletRequest request) {
        return ADMIN_ALLOWED_IPS.stream().anyMatch(matcher -> matcher.matches(request));
    }

    private boolean isExactPath(HttpServletRequest request, String path) {
        return (request.getContextPath() + path).equals(request.getRequestURI());
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:5173", 
        		"http://192.168.50.131:5173",
        		"http://192.168.50.156:5173",
        		"http://192.168.50.19:5173"));
        
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}