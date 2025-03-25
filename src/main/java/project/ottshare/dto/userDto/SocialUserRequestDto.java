package project.ottshare.dto.userDto;

import lombok.*;
import project.ottshare.enums.Role;
import project.ottshare.security.oauth2.OAuth2UserInfo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialUserRequestDto {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private Role role;

    public static SocialUserRequestDto from(OAuth2UserInfo userInfo) {
        return builder().
                username(userInfo.getProvider() + "_" + userInfo.getProviderId())
                .password(null)
                .email(userInfo.getEmail())
                .nickname(userInfo.getName() + "_" + userInfo.getProviderId())
                .role(Role.SOCIAL).build();
    }
}
