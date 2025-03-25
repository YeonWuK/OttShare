package project.ottshare.dto.userDto;

import lombok.*;
import project.ottshare.entity.User;
import project.ottshare.enums.BankType;
import project.ottshare.enums.Role;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long userId;

    private String name;

    private String username;

    private String password;

    private String email;

    private String nickname;

    private String phoneNumber;

    private BankType bank;

    private String account;

    private String accountHolder;

    private Role role;

    private boolean isShareRoom;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .bank(user.getBank())
                .account(user.getAccount())
                .accountHolder(user.getAccountHolder())
                .role(user.getRole())
                .isShareRoom(user.isShareRoom())
                .build();
    }
}
