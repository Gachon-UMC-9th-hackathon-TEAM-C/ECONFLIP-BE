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

        /* ===================== 네이버 ===================== */
        if ("naver".equals(registrationId)) {

            Object responseObj = oAuth2User.getAttributes().get("response");

            if (!(responseObj instanceof Map)) {
                throw new IllegalStateException("Naver OAuth response is not a Map");
            }

            Map<String, Object> response = (Map<String, Object>) responseObj;

            Object idObj = response.get("id");
            if (idObj == null) {
                throw new IllegalStateException("Naver OAuth response is missing id");
            }

            String socialId = idObj.toString();

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

        /* ===================== 카카오 ===================== */
        if ("kakao".equals(registrationId)) {

            Map<String, Object> attributes = oAuth2User.getAttributes();

            Object idObj = attributes.get("id");
            if (idObj == null) {
                throw new IllegalStateException("Kakao OAuth response is missing id");
            }

            String socialId = idObj.toString();

            Object kakaoAccountObj = attributes.get("kakao_account");
            if (!(kakaoAccountObj instanceof Map)) {
                throw new IllegalStateException("Kakao OAuth response is missing kakao_account");
            }

            Map<String, Object> kakaoAccount =
                    (Map<String, Object>) kakaoAccountObj;

            Object profileObj = kakaoAccount.get("profile");
            if (!(profileObj instanceof Map)) {
                throw new IllegalStateException("Kakao OAuth response is missing profile");
            }

            Map<String, Object> profile =
                    (Map<String, Object>) profileObj;

            String name = profile.get("nickname") != null
                    ? profile.get("nickname").toString()
                    : "카카오유저";

            String imageUrl = profile.get("profile_image_url") != null
                    ? profile.get("profile_image_url").toString()
                    : null;

            User user = userRepository
                    .findBySocialTypeAndSocialId(SocialType.KAKAO, socialId)
                    .orElseGet(() ->
                            userRepository.save(
                                    User.createSocialUser(
                                            SocialType.KAKAO,
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
