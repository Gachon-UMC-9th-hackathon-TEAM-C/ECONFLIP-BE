package com.example.econflip.global.config.security.oauth;

import com.example.econflip.domain.user.entity.User;
import com.example.econflip.domain.user.enums.SocialType;
import com.example.econflip.domain.user.repository.UserRepository;
import com.example.econflip.global.config.security.oauth.exception.OAuthException;
import com.example.econflip.global.config.security.oauth.exception.code.OAuthErrorCode;
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
                throw new OAuthException(OAuthErrorCode.OAUTH_RESPONSE_MAPPING_FAILED);
            }

            Map<String, Object> response = (Map<String, Object>) responseObj;

            Object idObj = response.get("id");
            // 필수 식별자 (id) 가 없을때 예외처리
            if (idObj == null) {
                throw new OAuthException(OAuthErrorCode.OAUTH_RESPONSE_MISSING_ID);
            }

            String socialId = idObj.toString();

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
                                            imageUrl
                                    )
                            )
                    );

            return new CustomOAuth2User(user, oAuth2User.getAttributes());
        }
        /* ===================== 카카오 ===================== */
        else if ("kakao".equals(registrationId)) {

            Map<String, Object> attributes = oAuth2User.getAttributes();

            Object idObj = attributes.get("id");
            // 필수 식별자(id)가 없을때
            if (idObj == null) {
                throw new OAuthException(OAuthErrorCode.OAUTH_RESPONSE_MISSING_ID);
            }

            String socialId = idObj.toString();

            Map<String, Object> kakaoAccount =
                    (Map<String, Object>) attributes.get("kakao_account");

            Map<String, Object> profile =
                    kakaoAccount != null
                            ? (Map<String, Object>) kakaoAccount.get("profile")
                            : null;

            String imageUrl = profile != null
                    ? (String) profile.get("profile_image_url")
                    : null;

            User user = userRepository
                    .findBySocialTypeAndSocialId(SocialType.KAKAO, socialId)
                    .orElseGet(() ->
                            userRepository.save(
                                    User.createSocialUser(
                                            SocialType.KAKAO,
                                            socialId,
                                            imageUrl
                                    )
                            )
                    );

            return new CustomOAuth2User(user, attributes);
        }
        throw new OAuthException(OAuthErrorCode.NOT_SUPPORTED_OAUTH);
    }
}
