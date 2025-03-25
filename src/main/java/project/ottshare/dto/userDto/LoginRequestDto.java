package project.ottshare.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.ottshare.validation.ValidationGroups;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "Login Id 값은 필수 입력 사항입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private String username;

    @NotBlank(message = "Login password 값은 필수 입력 사항입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private String password;
}
