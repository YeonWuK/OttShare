package project.ottshare.dto.ottShareRoomDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.ottshare.dto.sharingUserDto.SharingUserResponseDto;
import project.ottshare.dto.waitingUserDto.WaitingUserResponseDto;
import project.ottshare.enums.OttType;
import project.ottshare.validation.ValidationGroups;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OttShareRoomRequestDto {

    @Builder.Default
    private List<SharingUserResponseDto> sharingUserResponseDTOS = new ArrayList<>();

    @NotBlank(message = "OttType 은 필수 항목 사항입니다.", groups = ValidationGroups.NotBlankGroups.class)
    private OttType ottType;

    private String ottId;

    private String ottPassword;

    public static OttShareRoomRequestDto from(List<WaitingUserResponseDto> waitingUsers, String ottId, String ottPassword, OttType ottType) {
        return OttShareRoomRequestDto.builder()
                .sharingUserResponseDTOS(waitingUsers.stream()
                        .map(SharingUserResponseDto::from)
                        .collect(Collectors.toList()))
                .ottType(ottType)
                .ottId(ottId)
                .ottPassword(ottPassword)
                .build();
    }
}
