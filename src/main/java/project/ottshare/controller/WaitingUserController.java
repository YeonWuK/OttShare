package project.ottshare.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.ottshare.config.RedisConfig;
import project.ottshare.dto.ottShareRoomDto.OttShareRoomRequestDto;
import project.ottshare.dto.waitingUserDto.WaitingUserRequestDto;
import project.ottshare.dto.waitingUserDto.WaitingUserResponseDto;
import project.ottshare.security.auth.CustomUserDetails;
import project.ottshare.service.OttShareRoomService;
import project.ottshare.service.WaitingUserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/waiting-user")
@RequiredArgsConstructor
@Slf4j
public class WaitingUserController {

    private final WaitingUserService waitingUserService;
    private final OttShareRoomService ottShareRoomService;

    /**
     * user 삭제
     */
    @DeleteMapping
    public ResponseEntity<String> deleteWaitingUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        waitingUserService.deleteWaitingUser(customUserDetails.userId());

        return ResponseEntity.ok("delete waiting user");
    }

    /**
     *  ShareRoom 생성
     */    @PostMapping
    public ResponseEntity<String> createOttShareRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     @RequestBody WaitingUserRequestDto waitingUserRequestDTO) throws Exception {
        log.info("✅ isLeader Value: {}", waitingUserRequestDTO.isLeader());

        waitingUserService.createWaitingUser(customUserDetails.userId(), waitingUserRequestDTO);

        List<WaitingUserResponseDto> members = waitingUserService.NonLeaderByOtt(waitingUserRequestDTO.getOttType());
        if (members.isEmpty()){
            return ResponseEntity.ok("멤버를 기다리는 중입니다.");
        }

        Optional<WaitingUserResponseDto> leaderByOtt = waitingUserService.getLeaderByOtt(waitingUserRequestDTO.getOttType());
        if (leaderByOtt.isEmpty()){
            return ResponseEntity.ok("방장이 없습니다.");
        }

        WaitingUserResponseDto leader = leaderByOtt.get();
        members.add(leader);
        waitingUserService.deleteAllWaitingUser(members);

        OttShareRoomRequestDto ottShareRoomRequestDTO = ottShareRoomService.convertToOttShareRoom(
                members,
                leader.getOttId(),
                leader.getOttPassword(),
                waitingUserRequestDTO.getOttType()
        );

        ottShareRoomService.createOttShareRoom(ottShareRoomRequestDTO);

        return ResponseEntity.ok("Room created successfully.");
    }
}
