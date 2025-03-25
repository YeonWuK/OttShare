package project.ottshare.dto.sharingUserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.ottshare.dto.waitingUserDto.WaitingUserResponseDto;
import project.ottshare.entity.SharingUser;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class SharingUserResponseDto {
    private Long id;
    private Long userId;
    private Long ottShareRoomId;
    private Boolean isLeader;
    private Boolean isChecked;

    public static SharingUserResponseDto from(WaitingUserResponseDto dto) {
        return SharingUserResponseDto.builder()
                .userId(dto.getUserId()) // ✅ WaitingUserResponseDTO에서 유저 ID 가져오기
                .isLeader(dto.isLeader()) // ✅ 리더 여부 가져오기
                .isChecked(false) // ✅ 기본값 설정 (초기 생성 시 false)
                .build();
    }

    public static SharingUserResponseDto from(SharingUser sharingUser){
        return SharingUserResponseDto.builder()
                .id(sharingUser.getId())
                .userId(sharingUser.getUser().getId())
                .ottShareRoomId(sharingUser.getOttShareRoom().getId())
                .isLeader(sharingUser.getIsLeader())
                .isChecked(sharingUser.getIsChecked())
                .build();
    }
}