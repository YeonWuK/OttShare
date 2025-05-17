package project.ottshare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import project.ottshare.dto.messageDto.MessageRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchangeName, MessageRequestDto message, Long roomId, Long userId) {
        String routingKey = "chat.room." + roomId;
        log.info("Sending message to RabbitMQ | Exchange: {} | RoutingKey: {} | Message: {} | RoomId: {} | userId : {}",
                exchangeName, routingKey, message, roomId, userId);

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
