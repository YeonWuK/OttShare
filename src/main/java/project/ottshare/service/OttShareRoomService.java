package project.ottshare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ottshare.aop.DistributeLock;
import project.ottshare.dto.ottShareRoomDto.OttShareRoomRequestDto;
import project.ottshare.dto.ottShareRoomDto.OttShareRoomResponseDto;
import project.ottshare.dto.sharingUserDto.SharingUserResponseDto;
import project.ottshare.dto.waitingUserDto.WaitingUserResponseDto;
import project.ottshare.entity.OttShareRoom;
import project.ottshare.entity.SharingUser;
import project.ottshare.entity.User;
import project.ottshare.entity.WaitingUser;
import project.ottshare.enums.OttType;
import project.ottshare.exception.OttSharingRoomNotFoundException;
import project.ottshare.exception.SharingUserNotCheckedException;
import project.ottshare.exception.UserNotFoundException;
import project.ottshare.repository.OttShareRoomRepository;
import project.ottshare.repository.SharingUserRepository;
import project.ottshare.repository.UserRepository;
import project.ottshare.repository.WaitingUserRepository;
import project.ottshare.repository.custom.WaitingUserRepositoryCustomImpl;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OttShareRoomService {
    private final OttShareRoomRepository ottShareRoomRepository;
    private final SharingUserRepository sharingUserRepository;
    private final WaitingUserRepository waitingUserRepository;
    private final SharingUserService sharingUserService;
    private final UserRepository userRepository;
    private final WaitingUserRepositoryCustomImpl waitingUserRepositoryCustomImpl;

    /**
     * WaitingUserResponseDTO -> OttShareRoomRequestDTO 변환
     */
    public OttShareRoomRequestDto convertToOttShareRoom(List<WaitingUserResponseDto> waitingUsers, String ottId, String ottPassword, OttType ottType) {

        if (waitingUsers.isEmpty()) {
            throw new UserNotFoundException("대기 중인 사용자가 없습니다.");
        }

        return OttShareRoomRequestDto.from(waitingUsers, ottId, ottPassword, ottType);
    }

    /**
     * ott 공유방 생성
     */
    @Transactional
    public void createOttShareRoom(OttShareRoomRequestDto requestDTO) {
        Map<Long, User> userMap = sharingUserService.getUserMapByDto(requestDTO);

        OttShareRoom ottShareRoom = OttShareRoom.from(requestDTO, userMap);

        List<Long> userIds = requestDTO.getSharingUserResponseDTOS().stream()
                .map(SharingUserResponseDto::getUserId)
                .toList();

//        userMap.values().forEach(User::checkShareRoom); ✅Repository 커스텀으로 N+1문제가 발생할 수 있는 원인 수정
        userRepository.UpdateIsShareRoom(userIds);

        ottShareRoomRepository.save(ottShareRoom);
    }


    /**
     * ott 공유방 전체 삭제
     */
    @Transactional
    public void deleteOttShareRoom(Long roomId, Long userId) {
        log.info("Leader 로그인 접근자 userId: {}", userId);

        sharingUserRepository.findUserByOttShareRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new OttSharingRoomNotFoundException(roomId, userId));

        // 커스텀으로 RoomId와 연관된 userIds 가져오기
        List<Long> userIds = sharingUserRepository.findAllUserIdsByOttShareRoomId(roomId);

        // 기존 User IsShareRoom false 로 해제
        userRepository.UpdateUnShareRoom(userIds);

        // (CascadeType.ALL 로 인해 SharingUser 도 함께 삭제됨)
        ottShareRoomRepository.deleteById(roomId);

        log.info("Removed OttShareRoom with ID: {}", roomId);
    }


    /**
     * ott 공유방 강제퇴장
     */
    @Transactional
    public void expelUserFromRoom(Long roomId, Long userId){
        SharingUser sharingUser = sharingUserRepository.findUserByOttShareRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new OttSharingRoomNotFoundException(roomId, userId));

        sharingUser.getUser().leaveShareRoom();
        // sharingUserRepository.save(sharingUser); 필요 ❌ JPA 가 자동으로 변경감지

        // sharingUserId에 있는 값으로 부터 OttShareRoom 가져오기
        OttShareRoom ottShareRoom = sharingUser.getOttShareRoom();

        // ottShareRoom 연관된 SharingUser 삭제
        ottShareRoom.removeSharingUser(sharingUser);

        sharingUserRepository.delete(sharingUser);
        log.info("Expelled user with ID: {} from room ID: {}", userId, roomId);
    }

    /**
     * 방나가기
     */
    @Transactional
    public void leaveRoom(Long roomId, Long userId) {
        SharingUser sharingUser = sharingUserRepository.findUserByOttShareRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new OttSharingRoomNotFoundException(roomId, userId));

        sharingUser.getUser().leaveShareRoom();

        OttShareRoom ottShareRoom = sharingUser.getOttShareRoom();
        ottShareRoom.removeSharingUser(sharingUser);

        sharingUserRepository.delete(sharingUser);
        log.info("Leave room with ID: {} from room ID: {}", userId, roomId);
    }


    /**
     * 체크 기능
     */
    public void checkUserInRoom(Long roomId, Long userId) {
        SharingUser sharingUser = sharingUserRepository.findUserByOttShareRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new OttSharingRoomNotFoundException(roomId, userId));

        sharingUser.checkRoom();
        log.info("Checked user with ID: {} in room ID: {}", userId, roomId);
    }


    /**
     * 아이디, 비밀번호 확인
     */
    public OttShareRoomResponseDto getRoomIdAndPassword(Long roomId, Long userId) {
        SharingUser sharingUser = sharingUserRepository.findUserByOttShareRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new OttSharingRoomNotFoundException(roomId, userId));

        OttShareRoom ottShareRoom = sharingUser.getOttShareRoom();

        if (!sharingUser.getIsChecked()) {
            throw new SharingUserNotCheckedException(userId);
        }

        return OttShareRoomResponseDto.from(ottShareRoom);
    }


    /**
     * 새로운 맴버 찾기
     */
    @DistributeLock(key = "#roomId")
    @Transactional
    public boolean findNewMember(Long roomId) {
        OttShareRoom ottShareRoom = ottShareRoomRepository.findById(roomId)
                .orElseThrow(() -> new OttSharingRoomNotFoundException("존재하지 않는 방입니다. roomId :"+ roomId));

        OttType ottType = ottShareRoom.getOttType();
        List<WaitingUser> newNonLeader = waitingUserRepositoryCustomImpl.findNonLeaderByOtt(ottType, 1);

        if (!newNonLeader.isEmpty()) {
            WaitingUser waitingUser = newNonLeader.get(0);
            SharingUser newSharingUser = SharingUser.from(waitingUser, ottShareRoom);

            ottShareRoom.addSharingUser(newSharingUser);
            waitingUserRepository.delete(waitingUser);

            log.info("Added new member to room ID: {}", roomId);
            return true;  // 멤버를 찾은 경우 true 반환

        }
        log.warn("Not find new member to room ID: {}", roomId);
        return false;
    }


    /**
     * SharingUserResponseDTO 에서 가져오기
     */
    public OttShareRoomResponseDto getOttShareRoom(SharingUserResponseDto sharingUserResponseDTO) {
        OttShareRoom ottShareRoom = ottShareRoomRepository.findById(sharingUserResponseDTO.getOttShareRoomId())
                .orElseThrow(() -> new OttSharingRoomNotFoundException("방을 찾지 못하였습니다."));

        return OttShareRoomResponseDto.from(ottShareRoom);
    }

    public Long isRoomExist(Long id) {

        boolean check = ottShareRoomRepository.existsById(id);

        if (!check){
            throw new OttSharingRoomNotFoundException("해당 방이 존재하지 않습니다! : " + id);
        }
        return id;
    }

}
