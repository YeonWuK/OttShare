package project.ottshare.dto.waitingUserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.ottshare.entity.WaitingUser;
import project.ottshare.enums.OttType;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WaitingUserResponseDto {
    private Long id;

    private Long userId; // ✅ 추가 (User ID 저장)

    private OttType ott;

    private String ottId;

    private String ottPassword;

    private boolean isLeader;


    public static WaitingUserResponseDto from(WaitingUser waitingUser) {
        return WaitingUserResponseDto.builder()
                .id(waitingUser.getId())
                .userId(waitingUser.getUser().getId())
                .ott(waitingUser.getOttType())
                .ottId(waitingUser.getOttId())
                .ottPassword(waitingUser.getOttPassword())
                .isLeader(waitingUser.getIsLeader())
                .build();
    }
}
