package project.ottshare.dto.waitingUserDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.ottshare.enums.OttType;
import project.ottshare.validation.ValidationGroups;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingUserRequestDto {

    @NotBlank(message = "ottType은 필수입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private OttType ottType;

    private String ottId;

    private String ottPassword;

    @NotBlank(message = "leader 선택은 필수입니다.", groups = ValidationGroups.NotBlankGroups.class)
    @JsonProperty("isLeader") // ✅ JSON 필드명 강제 설정
    private boolean isLeader;
}
