package project.ottshare.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.ottshare.validation.ValidationGroups;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindPasswordRequestDto {
    @NotBlank(message = "이름은 필수 입력 값입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private String name;

    @NotBlank(message = "ID 는 필수 입력 값입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private String username;

    @NotBlank(message = "이메일은 필수 입력 값입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private String email;
}
