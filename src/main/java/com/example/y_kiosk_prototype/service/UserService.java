package com.example.y_kiosk_prototype.service;

import com.example.y_kiosk_prototype.entity.SocialLogin;
import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.repository.SocialLoginRepository;
import com.example.y_kiosk_prototype.repository.UserAccountRepository;
import com.example.y_kiosk_prototype.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
