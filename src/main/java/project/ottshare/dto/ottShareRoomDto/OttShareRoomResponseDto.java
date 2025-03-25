package project.ottshare.dto.ottShareRoomDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.ottshare.entity.OttShareRoom;
import project.ottshare.enums.OttType;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class OttShareRoomResponseDto {
    private Long id;
    private String ottId;
    private OttType ottType;
    private String ottPassword;

    public static OttShareRoomResponseDto from(OttShareRoom ottShareRoom) {
        return OttShareRoomResponseDto.builder()
                .id(ottShareRoom.getId())
                .ottId(ottShareRoom.getOttId())
                .ottType(ottShareRoom.getOttType())
                .ottPassword(ottShareRoom.getOttPassword())
                .build();
    }
}
