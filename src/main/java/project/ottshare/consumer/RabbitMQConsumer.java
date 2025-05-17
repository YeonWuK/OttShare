package project.ottshare.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import project.ottshare.dto.messageDto.MessageRequestDto;
import project.ottshare.entity.Message;
import project.ottshare.entity.OttShareRoom;
import project.ottshare.entity.SharingUser;
import project.ottshare.exception.OttSharingRoomNotFoundException;
import project.ottshare.exception.SharingUserNotFoundException;
import project.ottshare.repository.MessageRepository;
import project.ottshare.repository.OttShareRoomRepository;
import project.ottshare.repository.SharingUserRepository;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final MessageRepository messageRepository;
    private final SharingUserRepository sharingUserRepository;
    private final OttShareRoomRepository ottShareRoomRepository;

    @RabbitListener(queues = "#{@queueNameProvider.getQueueNames()}")
    public void consumeMessage(MessageRequestDto message) {

        SharingUser sharingUser = sharingUserRepository.findByUserId(message.getSharingUserId()).orElseThrow(() ->
                new SharingUserNotFoundException(message.getSharingUserId()));
        OttShareRoom ottShareRoom = ottShareRoomRepository.findById(message.getOttShareRoomId()).orElseThrow(() ->
                new OttSharingRoomNotFoundException(("해당 채팅방이 존재하지 않습니다.")));

        Message enMessage = Message.from(message.getMessage(), ottShareRoom, sharingUser);
        messageRepository.save(enMessage);
    }
}
