package project.ottshare.security.oauth2;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        return (String) attributes.get("kakao_provider_id");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("kakao_email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("kakao_name");
    }
}
