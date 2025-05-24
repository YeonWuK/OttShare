package project.ottshare.dto.messageDto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {

    @NotNull(message = "Message cannot be null")
    private String message;

    private Long ottShareRoomId;

    private Long sharingUserId;

    public static MessageRequestDto from(Long ott_share_room_id, Long sharing_user_id, String message) {
        return MessageRequestDto.builder()
                .ottShareRoomId(ott_share_room_id)
                .sharingUserId(sharing_user_id)
                .message(message)
                .build();
    }
}
