package project.ottshare.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ottshare.dto.messageDto.MessageRequestDto;
import project.ottshare.security.auth.CustomUserDetails;
import project.ottshare.service.OttShareRoomService;
import project.ottshare.service.RabbitMqService;
import project.ottshare.service.SharingUserService;
import project.ottshare.validation.ValidationGroups;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final RabbitMqService rabbitMqService;
    private final OttShareRoomService ottShareRoomService;
    private final SharingUserService sharingUserService;

    @PostMapping("/chat/{room-id}")
    public void sendChatMessage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                @Validated(ValidationGroups.NotBlankGroups.class) @RequestBody MessageRequestDto message,
                                @DestinationVariable("room-id") Long roomId) {

        log.info("Received message: {}", message.getMessage());

        Long room_id = ottShareRoomService.isRoomExist(roomId);
        Long user_id = customUserDetails.userId();

        sharingUserService.is_sharingUser(user_id);
        rabbitMqService.sendMessage("chat.exchange", message.getMessage(), room_id, user_id);
    }
}
