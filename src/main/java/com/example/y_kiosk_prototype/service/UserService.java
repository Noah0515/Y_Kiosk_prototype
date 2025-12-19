package com.example.y_kiosk_prototype.service;

import com.example.y_kiosk_prototype.entity.SocialLogin;
import com.example.y_kiosk_prototype.entity.UserInfo;
import com.example.y_kiosk_prototype.repository.SocialLoginRepository;
import com.example.y_kiosk_prototype.repository.UserAccountRepository;
import com.example.y_kiosk_prototype.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

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
        userInfoRepository.save(userInfo);
        socialLoginRepository.save(socialLogin);
    }

    public UserInfo findUserByUserId(String userId){
        return userInfoRepository.findByUserId(userId).get();
    }

    public SocialLogin findSocialLoginByKakaoId(String kakaoId){
        return socialLoginRepository.findByKakaoId(kakaoId).get();
    }
}
