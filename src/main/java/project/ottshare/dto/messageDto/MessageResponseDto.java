package project.ottshare.dto.messageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.ottshare.dto.ottShareRoomDto.OttShareRoomResponseDto;
import project.ottshare.dto.sharingUserDto.SharingUserResponseDto;
import project.ottshare.entity.Message;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
    private OttShareRoomResponseDto ottShareRoomResponseDTO;

    private SharingUserResponseDto sharingUserResponseDTO;

    private String message;

    // ✅ Entity -> DTO 변환 메서드
    public static MessageResponseDto from(Message message) {
        return MessageResponseDto.builder()
                .message(message.getMessage())
                .ottShareRoomResponseDTO(OttShareRoomResponseDto.from(message.getOttShareRoom()))
                .sharingUserResponseDTO(SharingUserResponseDto.from(message.getSharingUser()))
                .build();
    }
}
