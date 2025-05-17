package project.ottshare.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ottshare.dto.ottShareRoomDto.OttShareRoomRequestDto;
import project.ottshare.dto.sharingUserDto.SharingUserResponseDto;
import project.ottshare.entity.SharingUser;
import project.ottshare.entity.User;
import project.ottshare.exception.SharingUserNotFoundException;
import project.ottshare.exception.UserNotFoundException;
import project.ottshare.repository.SharingUserRepository;
import project.ottshare.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SharingUserService {
    private final UserRepository userRepository;
    private final SharingUserRepository sharingUserRepository;

    /**
     *  Map<Long, User>로 더 빠르게 찾기 가능
     */
    @Transactional
    public Map<Long, User> getUserMapByDto(OttShareRoomRequestDto dto) {
        List<Long> userIds = dto.getSharingUserResponseDTOS().stream()
                .map(SharingUserResponseDto::getUserId)
                .toList();

        List<User> users = userRepository.findAllById(userIds);

        return users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));
    }

    /**
     *  로그인한 userId 에서 SharingUserResponse 반환
     */
    public SharingUserResponseDto getSharingUserByUserId(Long userId) {
        SharingUser sharingUser = sharingUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return SharingUserResponseDto.from(sharingUser);
    }



    /**
     * 로그인한 userId 에서 Sharing중인지 여부 체크
     */
    public boolean is_sharingUser(Long userId) {
        return sharingUserRepository.isSharingUserByUserId(userId);
    }
}
