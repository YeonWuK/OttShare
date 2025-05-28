package project.ottshare.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.ottshare.dto.ottShareRoomDto.OttShareRoomResponseDto;
import project.ottshare.dto.sharingUserDto.SharingUserResponseDto;
import project.ottshare.security.auth.CustomUserDetails;
import project.ottshare.service.OttShareRoomService;
import project.ottshare.service.SharingUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/Ott-ShareRoom")
@Slf4j
public class OttShareRoomController {
    private final OttShareRoomService ottShareRoomService;
    private final SharingUserService sharingUserService;

    /**
     * 채팅방
     */
    @PostMapping
    public ResponseEntity<OttShareRoomResponseDto> getRoomDetails(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("Fetching OTT share room for user ID: {}", customUserDetails.userId());
        SharingUserResponseDto sharingUserResponseDTO = sharingUserService.getSharingUserByUserId(customUserDetails.userId());
        OttShareRoomResponseDto ottShareRoom = ottShareRoomService.getOttShareRoom(sharingUserResponseDTO);

        return ResponseEntity.ok(ottShareRoom);
    }


    /**
     * 강제퇴장
     */
    @DeleteMapping("/{room-id}/users/{user-id}")
    public ResponseEntity<?> expelUserFromRoom (@PathVariable("room-id") Long roomId, @PathVariable("user-id") Long userId) {

        log.info("Expelling userId: {}", userId);
        ottShareRoomService.expelUserFromRoom(roomId, userId);

        return ResponseEntity.ok().build();
    }


    /**
     * 스스로 채팅방 나가기
     */
    @DeleteMapping("/self")
    public ResponseEntity<?> deleteRoomSelf (@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.userId();
        SharingUserResponseDto sharingUserByUserId = sharingUserService.getSharingUserByUserId(userId);
        Long roomId = sharingUserByUserId.getOttShareRoomId();

        log.info("Deleting userId: {}, and roomId: {}", userId, roomId);

        if(sharingUserByUserId.getIsLeader()){
            ottShareRoomService.deleteOttShareRoom(roomId, userId);
        } else {
            ottShareRoomService.leaveRoom(roomId, userId);
        }
        return ResponseEntity.ok().build();
    }


    /**
     * 체크
     */
    @PostMapping("{room-id}/check")
    public ResponseEntity<?> checkUserInRoom(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @PathVariable("room-id") Long roomId) {
        Long userId = customUserDetails.userId();
        log.info("Checking if roomId: {}, userId: {}", roomId, userId);

        ottShareRoomService.checkUserInRoom(roomId, userId);

        return ResponseEntity.ok().build();
    }


    /**
     * 아이디, 비밀번호 확인
     */
    @PostMapping("/id-password")
    public ResponseEntity<?> getRoomIdAndPassword(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("Fetching ID and Password for user ID: {}", customUserDetails.userId());
        Long userId = customUserDetails.userId();

        SharingUserResponseDto sharingUserByUserId = sharingUserService.getSharingUserByUserId(userId);
        OttShareRoomResponseDto roomIdAndPassword = ottShareRoomService.getRoomIdAndPassword(sharingUserByUserId.getOttShareRoomId(), userId);

        return ResponseEntity.ok().body(roomIdAndPassword);
    }

    /**
     * 새로운 맴버 찾기
     */
    @GetMapping("{room-id}/findNewMember")
    public ResponseEntity<?> findNewMember(@PathVariable("room-id") Long roomId) {
        log.info("Fetching New Member for room ID: {}", roomId);
        boolean newMemberFound = ottShareRoomService.findNewMember(roomId);
        String message = newMemberFound ? "새로운 멤버를 찾았습니다" : "새로운 멤버를 찾지 못 했습니다";

        return ResponseEntity.ok().body(message);
    }
}
