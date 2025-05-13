package project.ottshare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ottshare.dto.waitingUserDto.WaitingUserRequestDto;
import project.ottshare.dto.waitingUserDto.WaitingUserResponseDto;
import project.ottshare.entity.User;
import project.ottshare.entity.WaitingUser;
import project.ottshare.enums.OttType;
import project.ottshare.exception.OttLeaderNotFoundException;
import project.ottshare.exception.OttNonLeaderNotFoundException;
import project.ottshare.exception.UserNotFoundException;
import project.ottshare.repository.UserRepository;
import project.ottshare.repository.WaitingUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class WaitingUserService {
    private final WaitingUserRepository waitingUserRepository;
    private final UserRepository userRepository;

    /**
     * user 저장
     */
    @Transactional
    public void createWaitingUser(Long userId, WaitingUserRequestDto waitingUserRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        WaitingUser waitingUser =  WaitingUser.from(waitingUserRequestDTO, user);
        waitingUserRepository.save(waitingUser);
    }

    /**
     * user 삭제
     */
    @Transactional
    public void deleteWaitingUser(Long id) {
        WaitingUser waitingUser = waitingUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        waitingUserRepository.delete(waitingUser);
    }


    /**
     * user 방 삭제
     */
    @Transactional
    public void deleteAllWaitingUser(List<WaitingUserResponseDto> waitingUserResponseDTOList) {
        waitingUserResponseDTOList.stream()
                .map(waitingUserResponseDTO -> waitingUserRepository.findById(waitingUserResponseDTO.getId())
                        .orElseThrow(() -> new UserNotFoundException(waitingUserResponseDTO.getId()))
                )
                .forEach(waitingUserRepository::delete);
    }

    /**
     * 리더가 있는지 확인
     */
    public WaitingUserResponseDto getLeaderByOtt(OttType ottType) {
        WaitingUser waitingUser = waitingUserRepository.findByIsLeaderTrueAndOttType(ottType)
                .orElseThrow(() -> new OttLeaderNotFoundException(ottType));

        return WaitingUserResponseDto.from(waitingUser);
    }


    /**
     * 리더가 아닌 user 가 모두 있는지 확인
     */
    public List<WaitingUserResponseDto> NonLeaderByOtt(OttType ottType) throws Exception {
        int nonLeaderCount = getNonLeaderCountByOtt(ottType); //NETFLIX 들어옴 그럼 int 2 로 반환
        List<WaitingUser> waitingUsers = waitingUserRepository.findByIsLeaderFalseAndOttType(ottType); //NETFLIX OttType찾기
        //컨트롤러에서 3명이나 방을 못만들면 이제 WaitingUserRepository 에다 저장됨 그럼 계속 인원을 확인하는거임.

        log.info("OttType 별 기대 인원수 : {}", nonLeaderCount);
        log.info("현재 대기 유저 수 : {}", waitingUsers.size());

        if (waitingUsers.size() != nonLeaderCount) {
            throw new OttNonLeaderNotFoundException(ottType); //예외 처리하기 나중에
        }

        return waitingUsers.stream()
                .map(WaitingUserResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 리더가 아닌 OttType 인원 확인
     */
    private int getNonLeaderCountByOtt(OttType ottType) {
        return switch (ottType) {
            case NETFLIX -> 2;
            case WAVVE, TVING -> 3;
        };
    }
}
