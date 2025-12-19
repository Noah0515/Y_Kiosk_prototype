package com.example.y_kiosk_prototype.login;

import com.example.y_kiosk_prototype.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;
    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    private Key key;    // jwt 서명에 사용할 암화화 키 객체

    @PostConstruct  // Bean 생성 후(의존성 주입 완료 후) 1회 실행
    protected void init(){
        // secretKey를 key 객체로 변환
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String createAccessToken(UserInfo user_info){
        // claims: jwt에 담을 정보
        Claims claims = Jwts.claims().setAudience(user_info.getUserId());
        claims.put("user_type", user_info.getUserType());
        // 현재 시간
        Date now = new Date();

        // jwt builder로 토큰 생성
        return Jwts.builder()
                .setClaims(claims)  // 정보 설정
                .setIssuedAt(now)   // 토큰발행시간
                .setExpiration(new Date(now.getTime() + accessTokenExpirationMs))   // 만료시간
                .signWith(key, SignatureAlgorithm.HS256)    // 서명: key를 이용해 hs256으로 암호화
                .compact();
    }

    public String createRefreshToken(UserInfo user_info){
        Date now = new Date();

        return Jwts.builder()
                .setSubject(user_info.getUserId())  // 정보 설정
                .setIssuedAt(now)   // 토큰발행시간
                .setExpiration(new Date(now.getTime() + refreshTokenExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)    // 서명: key를 이용해 hs256으로 암호화
                .compact();

    }

    public Authentication getAuthentication(String token){
        // key로 토큰 검증, claims 추출
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userId = claims.getSubject();
        String userType = claims.get("user_type", String.class);

        //Spring Security가 이해할 수 있는 userdetails 객체 생성
        //실제로는 db에서 정보를 가져와서 정석 --> 나중에 수정하자
        UserDetails userDetails = User.builder()
                .username(userId)
                .password("")
                .roles(userType)
                .build();

        // authentication 생성 후 리턴
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public boolean validateToken(String token){
        try {
            // 토큰 파싱
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            // 성공하면 true
            return true;
        } catch (Exception e) {
            // 실패하면 false
            return false;
        }
    }
}
