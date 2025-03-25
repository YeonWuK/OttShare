package project.ottshare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.ottshare.dto.sharingUserDto.SharingUserResponseDto;

import java.util.Map;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sharing_user")
public class SharingUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sharing_user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ott_share_room_id")
    private OttShareRoom ottShareRoom;

    @Column(name = "is_leader")
    private Boolean isLeader;

    @Column(name = "is_checked")
    private Boolean isChecked;

    public static SharingUser from(SharingUserResponseDto dto, OttShareRoom ottShareRoom, Map<Long, User> userMap) {

        User user = userMap.get(dto.getUserId());

        return SharingUser.builder()
                .user(user)
                .ottShareRoom(ottShareRoom)
                .isLeader(dto.getIsLeader())
                .isChecked(true)
                .build();
    }

    public void setOttShareRoom(OttShareRoom ottShareRoom) {
        this.ottShareRoom = ottShareRoom;
    }

    public void checkRoom(){
        this.isChecked = true;
    }

}
