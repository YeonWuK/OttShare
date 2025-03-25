package project.ottshare.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.ottshare.enums.BankType;
import project.ottshare.enums.Role;
import project.ottshare.validation.ValidationGroups;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "이름은 필수 입력 값입니다", groups = ValidationGroups.NotBlankGroups.class)
    private String name;

    @NotBlank(message = "아이디는 필수 입력 값입니다", groups = ValidationGroups.NotBlankGroups.class)
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "아이디는 특수문자를 제외한 4~20자 사이로 입력해주세요.", groups = ValidationGroups.PatternGroups.class)
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.", groups = ValidationGroups.NotBlankGroups.class)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?!.*\\s).{8,16}$", message = "비밀번호는 8~16자 사이로 영문, 특수문자 모두 포함해주세요.", groups = ValidationGroups.PatternGroups.class)
    private String password;

    @NotBlank(message = "이메일은 필수 입력 값입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private String email;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private String nickname;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private String phoneNumber;

    @NotBlank(message = "계좌번호는 필수 입력 값입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private String account;

    @NotBlank(message = "예금주는 필수 입력 값입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private String accountHolder;

    private BankType bank;

    private Role role;
}