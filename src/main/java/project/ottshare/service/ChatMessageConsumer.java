package project.ottshare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import project.ottshare.dto.messageDto.MessageRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener (queues = "${spring.rabbitmq.queue.name}")
    public void receiveMessage(@Payload MessageRequestDto message,
                               @Header("amqp_receivedRoutingKey") String routingKey) {

        String roomId = routingKey.split("\\.")[2];

        log.info("📩 Received message for room {}: {}", roomId, message.getMessage());

        messagingTemplate.convertAndSend("/topic/message/" + roomId, message);
    }
}
