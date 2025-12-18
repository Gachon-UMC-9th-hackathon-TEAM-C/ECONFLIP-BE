package com.example.econflip.global.config.security.oauth;

import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.enums.SocialType;
import com.example.econflip.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId =
                userRequest.getClientRegistration().getRegistrationId();

        // ✅ 네이버 로그인
        if ("naver".equals(registrationId)) {

            Map<String, Object> response =
                    (Map<String, Object>) oAuth2User.getAttributes().get("response");

            String socialId = response.get("id").toString();
            String name = response.get("name") != null
                    ? response.get("name").toString()
                    : "네이버유저";

            String imageUrl = response.get("profile_image") != null
                    ? response.get("profile_image").toString()
                    : null;

            User user = userRepository
                    .findBySocialTypeAndSocialId(SocialType.NAVER, socialId)
                    .orElseGet(() ->
                            userRepository.save(
                                    User.createSocialUser(
                                            SocialType.NAVER,
                                            socialId,
                                            name,
                                            imageUrl
                                    )
                            )
                    );

            return new CustomOAuth2User(user, oAuth2User.getAttributes());
        }

        throw new IllegalArgumentException("Unsupported OAuth provider: " + registrationId);
    }
}
