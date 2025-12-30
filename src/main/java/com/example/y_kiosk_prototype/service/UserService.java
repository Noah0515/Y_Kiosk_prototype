package com.example.y_kiosk_prototype.service;

import com.example.y_kiosk_prototype.data.UserType;
import com.example.y_kiosk_prototype.entity.SocialLogin;
import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.generator.IdGenerator;
import com.example.y_kiosk_prototype.repository.SocialLoginRepository;
import com.example.y_kiosk_prototype.repository.UserAccountRepository;
import com.example.y_kiosk_prototype.repository.UserInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final SocialLoginRepository socialLoginRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserInfoRepository userInfoRepository;

    public UserService(SocialLoginRepository socialLoginRepository, UserAccountRepository userAccountRepository, UserInfoRepository userInfoRepository) {
        this.socialLoginRepository = socialLoginRepository;
        this.userAccountRepository = userAccountRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public void addKakaoAccount(UserInfo userInfo, SocialLogin socialLogin){
        log.info("addKakaoAccount");
        userInfoRepository.save(userInfo);
        log.info("saved userInfo: " + userInfo.toString());
        socialLoginRepository.save(socialLogin);
        log.info("saved SocialLogin: " + socialLogin.toString());
    }

    public UserInfo findUserByUserId(String userId){
        return userInfoRepository.findByUserId(userId).get();
    }

    public Optional<SocialLogin> findSocialLoginByKakaoId(String kakaoId){
        return socialLoginRepository.findByKakaoId(kakaoId);
    }

    public String getNewUserId() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        String result = new Random().ints(60, 0, characters.length())
                .mapToObj(characters::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        return result;
    }

    @Transactional
    public UserInfo findOrCreateKakaoUser(Long kakaoId, String email) {
        String sKakaoId = String.valueOf(kakaoId);

        // 1. SocialLogin 레코드 찾기
        Optional<SocialLogin> socialLoginOpt = socialLoginRepository.findByKakaoId(sKakaoId);

        if (socialLoginOpt.isPresent()) {
            SocialLogin socialLogin = socialLoginOpt.get();
            log.info("Step 2. SocialLogin 레코드를 찾았습니다. (카카오ID: {})", socialLogin.getKakaoId());
            log.info("Step 3. 이 SocialLogin과 연결된 UserInfo(ID: {}) 조회를 시도합니다.", socialLogin.getUserId());

            try {
                // 여기가 에러가 났던 지점일 가능성이 높습니다.
                UserInfo userInfo = socialLogin.getUserInfo();

                if (userInfo != null) {
                    log.info("Step 4. UserInfo를 성공적으로 찾았습니다. (userId: {})", userInfo.getUserId());
                    return userInfo;
                } else {
                    // @OneToOne(optional = false)인데 null일 경우
                    log.error("Step 4-Error. SocialLogin은 있는데 연관된 UserInfo가 null입니다.");
                    throw new EntityNotFoundException("SocialLogin에 연결된 UserInfo가 없습니다.");
                }
            } catch (Exception e) {
                log.error("Step 4-Fatal. UserInfo를 가져오는 중 JPA 에러 발생: {}", e.getMessage());
                // 실제 DB에 UserInfo 데이터가 있는지, 60자 ID가 정확한지 확인해야 합니다.
                throw e;
            }

        } else {
            // 2. 가입되지 않은 경우 신규 가입 절차 진행
            log.info("Step 2-Notice. 기존 유저 정보 없음. 신규 가입을 진행합니다.");

            // UserInfo 생성 (여기서 사용자님의 60자 랜덤 ID 생성 로직을 넣으시면 됩니다)
            String newUserId = IdGenerator.generateStringId(60); // 사용자님이 만드신 랜덤 ID 생성 함수
            log.info("Step 3. 신규 UserInfo 생성 시도 (생성된 ID: {})", newUserId);

            UserInfo newUser = UserInfo.builder()
                    .userId(newUserId)
                    .name("카카오유저")
                    .userType(UserType.NORMAL)
                    .createAt(LocalDate.now())
                    .build();

            // SocialLogin 생성
            SocialLogin newSocial = SocialLogin.builder()
                    .kakaoId(sKakaoId)
                    .kakaoEmail(email)
                    .userInfo(newUser)
                    .build();

            log.info("Step 4. DB에 신규 계정 저장 시도 (UserInfo & SocialLogin)");
            addKakaoAccount(newUser, newSocial);

            log.info("Step 5. 신규 가입 및 저장 완료.");
            return newUser;
        }
        /*
        // 1. 이미 소셜 로그인이 등록된 유저인지 확인
        return socialLoginRepository.findByKakaoId(sKakaoId)
                .map(SocialLogin::getUserInfo) // 있다면 연관된 UserInfo 반환
                .orElseGet(() -> {
                    // 2. 없다면 신규 가입 절차 진행
                    log.info("신규 카카오 유저 가입 진행: {}", sKakaoId);

                    // UserInfo 생성 (userId는 임의의 UUID나 카카오ID 활용 가능)
                    String newUserId = "K_" + sKakaoId;
                    UserInfo newUser = UserInfo.builder()
                            .userId(newUserId)
                            .name("카카오유저") // 실제 개발 시 카카오 닉네임을 받아와서 넣을 수 있음
                            .userType(UserType.NORMAL)
                            .createAt(LocalDate.now())
                            .build();

                    // SocialLogin 생성
                    SocialLogin newSocial = SocialLogin.builder()
                            .kakaoId(sKakaoId)
                            .kakaoEmail(email)
                            .userInfo(newUser)
                            .build();

                    // 사용자님이 만든 메소드 호출 (UserInfo와 SocialLogin 저장)
                    addKakaoAccount(newUser, newSocial);

                    return newUser;
                });*/
    }
}
