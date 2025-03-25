package project.ottshare.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.ottshare.dto.userDto.UpdateUserDto;
import project.ottshare.dto.userDto.UserRequestDto;
import project.ottshare.dto.userDto.UserResponseDto;
import project.ottshare.entity.User;
import project.ottshare.exception.UserNotFoundException;
import project.ottshare.repository.UserRepository;
import project.ottshare.security.auth.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    //회원가입
    @Transactional
    public UserResponseDto registerUser(UserRequestDto dto) {

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User user = User.from(dto);
        userRepository.save(user);

        return UserResponseDto.from(user);
    }

    //회원삭제
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        //user 삭제
        userRepository.delete(user);
    }

    //로그아웃
    public boolean logout(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            long expireTime = jwtTokenProvider.extractAllClaims(token).getExpiration().getTime() - System.currentTimeMillis();

            tokenBlacklistService.addToBlacklist(token, expireTime);
            SecurityContextHolder.clearContext();
            return true;
        }
        return false;
    }

    //회원정보 수정
    @Transactional
    public void updateUser(long userId, UpdateUserDto updateRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.update(updateRequestDTO);
    }

    // 회원정보 가져오기
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        return UserResponseDto.from(user);
    }

    //임시 비밀번호 생성
    @Transactional
    public void updatePassword(String name, String username, String email, String newPassword){
        User user = findUserForPasswordReset(name, username, email);

        user.updatePassword(passwordEncoder.encode(newPassword));
    }

    //회원 검증
    public User findUserForPasswordReset(String name, String username, String email) {
        return userRepository.findByNameAndUsernameAndEmail(name, username, email)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));
    }
}
