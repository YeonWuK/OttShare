package project.ottshare.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.ottshare.dto.ottShareRoomDto.OttShareRoomRequestDto;
import project.ottshare.dto.waitingUserDto.WaitingUserRequestDto;
import project.ottshare.dto.waitingUserDto.WaitingUserResponseDto;
import project.ottshare.security.auth.CustomUserDetails;
import project.ottshare.service.OttShareRoomService;
import project.ottshare.service.WaitingUserService;

import java.util.List;

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

    //해당 부분 성능 향상 필요성
    @PostMapping
    public ResponseEntity<String> createOttShareRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                     @RequestBody WaitingUserRequestDto waitingUserRequestDTO) throws Exception {
        log.info("✅ isLeader Value: {}", waitingUserRequestDTO.isLeader());

        waitingUserService.createWaitingUser(customUserDetails.userId(), waitingUserRequestDTO);

        WaitingUserResponseDto leaderByOtt = waitingUserService.getLeaderByOtt(waitingUserRequestDTO.getOttType());
        List<WaitingUserResponseDto> members = waitingUserService.NonLeaderByOtt(waitingUserRequestDTO.getOttType());

        String ottId = leaderByOtt.getOttId();
        String ottPassword = leaderByOtt.getOttPassword();

        members.add(leaderByOtt);
        waitingUserService.deleteAllWaitingUser(members);

        OttShareRoomRequestDto ottShareRoomRequestDTO = ottShareRoomService.convertToOttShareRoom(members, ottId, ottPassword, waitingUserRequestDTO.getOttType());
        ottShareRoomService.createOttShareRoom(ottShareRoomRequestDTO);

        return ResponseEntity.ok("Room created successfully.");
    }
}
