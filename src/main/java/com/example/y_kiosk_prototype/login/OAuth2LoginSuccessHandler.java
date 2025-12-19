package com.example.y_kiosk_prototype.login;

import com.example.y_kiosk_prototype.data.UserType;
import com.example.y_kiosk_prototype.entity.SocialLogin;
import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.repository.SocialLoginRepository;
import com.example.y_kiosk_prototype.repository.UserInfoRepository;
import com.example.y_kiosk_prototype.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

// JWT 발급해주는 클래스
@Component  // spring bean 등록
@RequiredArgsConstructor    // 생성자 자동 등록
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("onAuthenticationSuccess");
        // OAuth2 사용자 정보 꺼내기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("attributes: {}", attributes);

        String email = null;
        String nickname = null;
        String providerId = null;
        try {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            //email = (String) profile.get("email");
            email = (String) kakaoAccount.get("email");
            nickname = (String) profile.get("nickname");
            providerId = String.valueOf(attributes.get("id"));  // 카카오 고유 아이디

            log.info("email: {}, nickname: {}, providerId: {}", email, nickname, providerId);
        } catch (Exception e) {
            log.error("데이터 추출 중 에러 발생: {}", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/login/required");
        }

        UserInfo userInfo;

        // db에서 찾아보고 기존 회원이 아니면 신규 등록
        // 근데 나는 로그인 옵션을 다양하게 줄거라 이건 개선해야됨.
        //Optional<SocialLogin> socialLogin = socialLoginRepository.findByKakaoId(providerId);
        Optional<SocialLogin> socialLogin= userService.findSocialLoginByKakaoId(providerId);

        if (socialLogin.isEmpty()) { // 없으면 신규회원으로 간주
            UserInfo newUser = UserInfo.builder()
                    .userId(createHash())
                    .userType(UserType.NORMAL)
                    .name(nickname)
                    .createAt(LocalDate.now())
                    .build();

            SocialLogin socialLogin1 = SocialLogin.builder()
                    .userId(newUser.getUserId())
                    .kakaoEmail(email)
                    .kakaoId(providerId)
                    .build();

            newUser.setUserId(userService.getNewUserId());
            socialLogin1.setUserId(newUser.getUserId());
            userService.addKakaoAccount(newUser, socialLogin1);
            userInfo = newUser;

        } else {
            SocialLogin existingSocialLogin = socialLogin.get();
            userInfo = userService.findUserByUserId(existingSocialLogin.getUserId());
        }

        //
        String accessToken =jwtTokenProvider.createAccessToken(userInfo);
        //String refreshToken =jwtTokenProvider.createRefreshToken(userInfo);

        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", accessToken)
                .path("/") // 모든 경로에서 쿠기 사용
                //.secure(true) // HTTPS에서만 전송. AWS 배포 시 이거 사용
                .secure(false) // 로컬 테스트에서는 false로 해야 동작
                .httpOnly(true) // js로 접근 불가(xss 방어)
                .sameSite("Lax") // CSRF 공격 방어
                .maxAge(60*60) // 한시간 뒤 파기
                .build();

        response.addHeader("Set-Cookie", jwtCookie.toString());

        String targetUrl = request.getContextPath() + "/main"; // 인증이 끝난 후 사용자가 볼 메인 화면. Context Path(/YKiosk 설정해줘야되니까 추가함.)
        // 만약 특정 위치에서 로그인을 시도하고 마지막 위치로 돌아간다면 targetUrl을 경우에 따라 바꿀 수 있도록
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }
    private String createHash(){
        return "";
    }
}
