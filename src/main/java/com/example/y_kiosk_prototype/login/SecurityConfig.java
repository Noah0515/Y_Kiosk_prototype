package com.example.y_kiosk_prototype.login;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration  // 보안 설정 파일
@EnableWebSecurity  // 보안 설정 파일
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("SecurityConfig : filterChain");
        http
                // React를 쓰는 경우 추가하는 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Spring boot만 쓸 때 설정
                .csrf(
                // 기존 보안 비활성화
                csrf -> csrf.disable()) // csrf 비활성화
                .formLogin(form -> form.disable())  // 기본 폼 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())    //HTTP basic 비활성화
                // jwt랄 사용하기 떄문에 서버에서 세션을 유지하지 않아 crsf공격에 대한 방어는 필요 없음
                // formLogin은 spring security가 제공하는 못생긴 로그인창이 떠서 따로 만들기
                // http basic은 매 요청마다 헤더에 id/pw를 보내는 방식인데 보안 취약점이 있어서 안씀. 대신 JWT를 씀

                // 세션 관리 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // sessionCreationPolicy를 비활성화 해서 서버가 세션을 저장하지 않음

                // 필터 배치
                // 만든 JwtFilter가 먼저 적용되도록
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                //  url 별 권한 관리
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(   // 누구나
                                "/", "/main", "/css/**", "/js/**", "/image/**", "/api/**", "/login/**", "/oauth2/**", "/auth/callback/**", "/auth/error/**",
                                "/api/auth/admin-login"
                        ).permitAll()
                        //.requestMatchers("/api/user/**").hasAnyRole("NORMAL", "MANAAGER")
                        //.requestMatchers("/api/admin/**").hasRole("ADMIN") // 관리자 전용 구역
                        //.requestMatchers("/api/user/**").hasRole("NORMAL") // 일반 사용자
                        //.requestMatchers("/api/user/**").hasRole("MANAGER") // 가게 manager 계정
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        // 인증이 안된 사람
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized"); // 안내 페이지로 안내
                        })
                        // 권한 없는 사람(ADMIN, NORMAL 같은 ROLE을 다루는거)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect(request.getContextPath() + "/login/required"); // 경고 페이지러 안내
                        })
                )
                // 소셜 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        // 소셜 로그인 성공하면 실행할 핸들러 등록
                        .successHandler(oAuth2LoginSuccessHandler)
                );

        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 프론트엔드 주소
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // React 주소 허용(vite 기본 주소)
        // 허용할 http 메소드
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 허용할 헤더 (인증 관견 헤더 등을 위해 다 허용)
        configuration.setAllowedHeaders(List.of("*"));
        // 쿠키 주고 받으려면 true로 해야됨
        configuration.setAllowCredentials(true); // 쿠키 전송 허용 (중요!)
        // URL 기반으로 설정을 관리하는 객체 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로(/**)에 위에서 만든 설정을 적용하겠다고 등록
        source.registerCorsConfiguration("/**", configuration);

        return source; // 이제 이 소스가 필터에 적용됩니다.
    }
}
