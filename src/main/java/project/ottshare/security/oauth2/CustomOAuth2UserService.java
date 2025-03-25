package project.ottshare.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ottshare.dto.userDto.SocialUserRequestDto;
import project.ottshare.entity.User;
import project.ottshare.repository.UserRepository;
import project.ottshare.security.auth.CustomUserDetails;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2UserInfo userInfo = getOAuth2UserInfo(registrationId,attributes);
        User user = createUserFromOAuth(userInfo);

        if (!userRepository.existsByUsername(user.getUsername())){
            userRepository.save(user);
        } else {
            user.update(user);
        }
        return new CustomUserDetails(user, attributes);
    }

    @Override
    public void setAttributesConverter(Converter<OAuth2UserRequest, Converter<Map<String, Object>, Map<String, Object>>> attributesConverter) {
        super.setAttributesConverter(attributesConverter);
    }

    private OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return new GoogleUserInfo(attributes);
        } else if (registrationId.equals("kakao")) {
            return new KakaoUserInfo(attributes);
        } else if (registrationId.equals("naver")) {
            return new NaverUserInfo((Map<String, Object>) attributes.get("response"));
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }
    }

    private User createUserFromOAuth(OAuth2UserInfo userinfo) {
        return User.from(SocialUserRequestDto.from(userinfo));
    }
}
