package project.ottshare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class Message extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ott_share_room_id")
    private OttShareRoom ottShareRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharing_user_id")
    private SharingUser sharingUser;

    @Column(name = "message", nullable = false)
    private String message;

    public static Message from(String message, OttShareRoom ottShareRoom, SharingUser sharingUser){
        return Message.builder()
                .ottShareRoom(ottShareRoom)
                .sharingUser(sharingUser)
                .message(message)
                .build();
    }
}
