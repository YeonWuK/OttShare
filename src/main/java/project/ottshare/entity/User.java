package project.ottshare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.ottshare.dto.userDto.UpdateUserDto;
import project.ottshare.dto.userDto.UserRequestDto;
import project.ottshare.dto.userDto.SocialUserRequestDto;
import project.ottshare.enums.BankType;
import project.ottshare.enums.Role;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nick_name", nullable = false, unique = true)
    private String nickname;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "account")
    private String account;

    @Column(name = "account_holder")
    private String accountHolder;

    @Column(name = "bank")
    @Enumerated(EnumType.STRING)
    private BankType bank;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_share_room", nullable = false, columnDefinition = "boolean default false")
    private boolean isShareRoom;

    //Oauth 회원 업데이트
    public void update(User user){
        this.name = user.name;
        this.username = user.username;
        this.password = user.password;
        this.nickname = user.nickname;
        this.phoneNumber = user.phoneNumber;
    }

    //기존유저 정보 업데이트
    public void update(UpdateUserDto dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.nickname = dto.getNickname();
    }

    //회원가입
    public static User from(UserRequestDto user) {
        return User.builder()
                .name(user.getName())
                .username(user.getUsername())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .account(user.getAccount())
                .accountHolder(user.getAccountHolder())
                .bank(user.getBank())
                .role(Role.USER)
                .build();
    }

    //Oauth 회원가입
    public static User from(SocialUserRequestDto user) {
        return User.builder()
                .id(builder().id)
                .username(user.getUsername())
                .role(Role.SOCIAL)
                .password(user.getPassword()).build();
    }

    //비즈니스 로직
    public void checkShareRoom(){
        this.isShareRoom = true;
    }

    public void leaveShareRoom(){
        this.isShareRoom = false;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
