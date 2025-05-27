package project.ottshare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.ottshare.dto.waitingUserDto.WaitingUserRequestDto;
import project.ottshare.enums.OttType;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "waiting_user")
public class WaitingUser extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waiting_user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ott_id")
    private String ottId;

    @Column(name = "ott_password")
    private String ottPassword;

    @Column(name = "is_leader")
    private Boolean isLeader;

    @Enumerated(EnumType.STRING)
    @Column(name = "ottType")
    private OttType ottType;

    public static WaitingUser from(WaitingUserRequestDto waitingUserRequestDTO, User user) {
        return WaitingUser.builder()
                .user(user)
                .ottId(waitingUserRequestDTO.getOttId())
                .ottPassword(waitingUserRequestDTO.getOttPassword())
                .isLeader(Boolean.TRUE.equals(waitingUserRequestDTO.isLeader())) // ✅ `null`이면 false, true면 유지
                .ottType(waitingUserRequestDTO.getOttType())
                .build();
    }
}