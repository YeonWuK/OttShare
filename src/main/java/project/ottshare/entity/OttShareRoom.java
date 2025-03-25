package project.ottshare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.ottshare.dto.ottShareRoomDto.OttShareRoomRequestDto;
import project.ottshare.enums.OttType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ott_share_room")
public class OttShareRoom extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ott_share_room_id")
    private Long id;

    @OneToMany(mappedBy = "ottShareRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SharingUser> sharingUsers = new ArrayList<>();

    @OneToMany(mappedBy = "ottShareRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "ott", nullable = false)
    private OttType ottType;

    @Column(name = "ott_id")
    private String ottId;

    @Column(name = "ott_password")
    private String ottPassword;


    public static OttShareRoom from(OttShareRoomRequestDto dto, Map<Long, User> user) {
        OttShareRoom room = OttShareRoom.builder()
                .ottType(dto.getOttType())
                .ottId(dto.getOttId())
                .ottPassword(dto.getOttPassword())
                .sharingUsers(new ArrayList<>())
                .build();

        List<SharingUser> sharingUsers = dto.getSharingUserResponseDTOS().stream()
                .map(userDto -> SharingUser.from(userDto, room, user))
                .toList();

        room.setSharingUsers(sharingUsers);

        return room;
    }

    public void setSharingUsers(List<SharingUser> sharingUsers) {
        if (this.sharingUsers == null) { // ✅ `null` 체크 추가
            this.sharingUsers = new ArrayList<>();
        }

        this.sharingUsers.clear(); // ✅ 기존 리스트 초기화
        this.sharingUsers.addAll(sharingUsers); // ✅ 새로운 리스트 추가
    }

    public void removeSharingUser(SharingUser sharingUser) {
        sharingUsers.remove(sharingUser);
        sharingUser.setOttShareRoom(null); // ✅ 관계 해제 (JPA가 변경 감지)
    }
}
