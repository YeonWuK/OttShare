package project.ottshare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import project.ottshare.dto.messageDto.MessageRequestDto;
import project.ottshare.entity.Message;
import project.ottshare.entity.OttShareRoom;
import project.ottshare.entity.SharingUser;
import project.ottshare.exception.OttSharingRoomNotFoundException;
import project.ottshare.exception.SharingUserNotFoundException;
import project.ottshare.repository.MessageRepository;
import project.ottshare.repository.OttShareRoomRepository;
import project.ottshare.repository.SharingUserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final OttShareRoomRepository ottShareRoomRepository;
    private final SharingUserRepository sharingUserRepository;

    @RabbitListener (queues = "${spring.rabbitmq.queue.name}")
    public void receiveMessage(@Payload MessageRequestDto message,
                               @Header("amqp_receivedRoutingKey") String routingKey) {

        SharingUser sharingUser = sharingUserRepository.findByUserId(message.getSharingUserId())
                .orElseThrow(() -> new SharingUserNotFoundException(message.getSharingUserId()));

        OttShareRoom room = ottShareRoomRepository.findById(message.getOttShareRoomId())
                .orElseThrow(() -> new OttSharingRoomNotFoundException("해당 채팅방이 존재하지 않습니다."));

        Message entity = Message.from(message.getMessage(), room, sharingUser);
        messageRepository.save(entity);

        String roomId = routingKey.split("\\.")[2];
        log.info("📩 Received message for room {}: {}", roomId, message.getMessage());

        messagingTemplate.convertAndSend("/topic/message/" + roomId, message);
    }
}