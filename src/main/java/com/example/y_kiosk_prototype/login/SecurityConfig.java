package com.example.y_kiosk_prototype.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration  // 보안 설정 파일
@EnableWebSecurity  // 보안 설정 파일
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("SecurityConfig : filterChain");
        http.csrf(
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
                                "/", "/main", "/css/**", "/js/**", "/image/**", "/login/**", "/oauth2/**", "/auth/callback/**", "/auth/error/**",
                                "/api/auth/admin-login"
                        ).permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // 관리자 전용 구역
                        .requestMatchers("/api/normal/**").hasRole("NORMAL") // 일반 사용자
                        .requestMatchers("/api/manager/**").hasRole("MANAGER") // 가게 manager 계정
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        // 인증이 안된 사람
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendRedirect("/login/required"); // 안내 페이지로 안내
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
}
