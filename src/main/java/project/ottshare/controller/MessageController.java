package project.ottshare.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ottshare.dto.messageDto.MessageRequestDto;
import project.ottshare.service.OttShareRoomService;
import project.ottshare.service.RabbitMqService;
import project.ottshare.validation.ValidationGroups;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final RabbitMqService rabbitMqService;
    private final OttShareRoomService ottShareRoomService;

    @MessageMapping("/chat/{room-id}")
    public void sendChatMessage(@Validated(ValidationGroups.NotBlankGroups.class) @RequestBody MessageRequestDto message,
                                @DestinationVariable("room-id") Long roomId) {
        log.info("Received message: {}", message.getMessage());

        ottShareRoomService.isRoomExist(roomId);

        rabbitMqService.sendMessage("chat.exchange", message, roomId);

    }
}
