package com.example.y_kiosk_prototype.login;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // 매 요청당 한번만 처리하는 필터
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            log.info("OPTIONS");
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveTokenFromCookie(request);

        // 쿠키에서 토큰 꺼내기
        if (token != null && jwtTokenProvider.validateToken(token)) {

            Authentication authentication = jwtTokenProvider.getAuthentication(token); // 토큰의 신분증인 authentication을

            //SecurityContextHolder.getContext().setAuthentication(authentication); // 이렇게 저장함. 그러면 해당 요청(Thread)가 끝날때까지 확인 가능
            SecurityContext context = SecurityContextHolder.createEmptyContext(); // 깨끗한 새 context를 만들고
            context.setAuthentication(authentication); //새로만든거에 설정하기
            SecurityContextHolder.setContext(context);

        }
        filterChain.doFilter(request, response);
    }

    // 쿠키 뒤지기
    private String resolveTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            log.info("Bearer token: {}", bearerToken);
            return bearerToken.substring(7);
        }
        return null;
    }
}
