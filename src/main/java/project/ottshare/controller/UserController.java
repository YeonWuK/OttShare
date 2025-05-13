package project.ottshare.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.ottshare.dto.userDto.*;
import project.ottshare.security.auth.CustomUserDetails;
import project.ottshare.security.auth.JwtTokenProvider;
import project.ottshare.service.TokenBlacklistService;
import project.ottshare.service.UserService;
import project.ottshare.validation.CustomValidators;
import project.ottshare.validation.ValidationGroups;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user") // ✅ 올바른 엔드포인트 매핑
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final CustomValidators validators;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * 회원가입
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated(ValidationGroups.NotBlankGroups.class) @RequestBody UserRequestDto registerRequestDTO,
                                          BindingResult bindingResult) {

        validators.joinValidateAll(registerRequestDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        UserResponseDto userResponse = userService.registerUser(registerRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        boolean logout = userService.logout(request);

        if (logout){
            return ResponseEntity.ok("성공적으로 블랙리스트에 접근!");
        }

        return ResponseEntity.ok("실패!");
    }

    /**
     * 회원 수정
     */
    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        @RequestBody UpdateUserDto updateUserDTO, BindingResult bindingResult) {

        validators.modifyValidateAll(updateUserDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        userService.updateUser(customUserDetails.userId() ,updateUserDTO);
        UserResponseDto updatedUser = userService.getUser(customUserDetails.userId());

        return ResponseEntity.ok(updatedUser);
    }

    /**
     * 마이페이지
     */
    @PostMapping
    public ResponseEntity<?> myPage(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        UserResponseDto userResponse = userService.getUser(customUserDetails.userId());

        return ResponseEntity.ok(userResponse);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.deleteUser(customUserDetails.userId());
        return ResponseEntity.ok("User Deleted successfully");
    }

    /**
     * 비밀번호 찾기
     */
    @PostMapping("/find-password")
    public ResponseEntity<?> findPassword(@RequestBody FindPasswordRequestDto dto) {
        log.info("Name: {}, Username: {}, Email: {}", dto.getName(), dto.getUsername(), dto.getEmail());
        String temporaryPassword = PasswordGenerator.generatePassword(10);
        userService.updatePassword(dto.getName(), dto.getUsername(), dto.getEmail(), temporaryPassword);

        return ResponseEntity.ok("임시 비밀번호는 " + temporaryPassword + "입니다.");
    }

    private ResponseEntity<?> buildValidationErrorResponse(BindingResult bindingResult) {
        List<String> errorMessages = bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorMessages);
    }

    /**
     *  accessToken 재발급
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No refresh token provided");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if(refreshToken == null || jwtTokenProvider.isTokenExpired(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is null or Expired");
        }

        if(tokenBlacklistService.isBlacklisted(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is blacklisted");
        }

        String username = jwtTokenProvider.extractUsername(refreshToken);
        if (!jwtTokenProvider.isValidateToken(refreshToken, username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String accessToken = jwtTokenProvider.generateToken(username);
        TokenResponse tokenResponse = new TokenResponse(accessToken);

        response.setHeader("Authorization", "Bearer " + accessToken);

        return ResponseEntity.ok("access_token : "+ tokenResponse);
    }
}
