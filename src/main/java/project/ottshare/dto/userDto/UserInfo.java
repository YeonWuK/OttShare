package project.ottshare.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.ottshare.enums.BankType;
import project.ottshare.enums.Role;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long userId;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String name;
    private String phoneNumber;
    private BankType bank;
    private String account;
    private String accountHolder;
    private Role role;
    private boolean isShareRoom;
}