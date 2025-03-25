package project.ottshare.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import project.ottshare.dto.userDto.UpdateUserDto;
import project.ottshare.dto.userDto.UserRequestDto;
import project.ottshare.repository.UserRepository;
import project.ottshare.security.auth.CustomUserDetails;

@Component
@RequiredArgsConstructor
public class CustomValidators {

    private final UsernameJoinValidator usernameJoinValidator;
    private final NicknameJoinValidator nicknameJoinValidator;

    private final UsernameModifyValidator usernameModifyValidator;
    private final UserNicknameModifyValidator userNicknameModifyValidator;

    /**
     * 회원가입 username, nickname 중복 검증
     */
    public void joinValidateAll(UserRequestDto dto, BindingResult bindingResult) {
        usernameJoinValidator.doValidate(dto, bindingResult);
        nicknameJoinValidator.doValidate(dto, bindingResult);
    }

    /**
     * 회원정보 수정 username, nickname 중복 검증
     */
    public void modifyValidateAll(UpdateUserDto dto, BindingResult bindingResult) {
        usernameModifyValidator.doValidate(dto, bindingResult);
        userNicknameModifyValidator.doValidate(dto, bindingResult);
    }

    /**
     * 회원가입 아이디 중복 검증
     */
    @Component
    @RequiredArgsConstructor
    public static class UsernameJoinValidator extends AbstractValidator<UserRequestDto> {

        UserRepository userRepository;
        @Override
        protected void doValidate(UserRequestDto dto, Errors errors) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                errors.rejectValue("username", "아이디 중복 오류", "이미 사용중인 아이디 입니다.");
            }
        }
    }

    /**
     * 회원가입 닉네임 중복 검증
     */
    @Component
    @RequiredArgsConstructor
    public static class NicknameJoinValidator extends AbstractValidator<UserRequestDto> {

        UserRepository userRepository;
        @Override
        protected void doValidate(UserRequestDto dto, Errors errors) {
            if (userRepository.existsByUsername(dto.getNickname())) {
                errors.rejectValue("nickname", "닉네임 중복 오류", "이미 사용중인 닉네임 입니다.");
            }
        }
    }

    /**
     * 회원 수정 아이디 중복 검증
     */
    @Component
    @RequiredArgsConstructor
    public static class UsernameModifyValidator extends AbstractValidator<UpdateUserDto> {
        UserRepository userRepository;

        @Override
        protected void doValidate(UpdateUserDto dto, Errors errors) {
            String currentUsername = getCurrentUsername();

            if(!dto.getUsername().equals(currentUsername) && userRepository.existsByUsername(dto.getUsername())) {
                errors.rejectValue("username", "아이디 중복 오류", "이미 사용중인 아이디 입니다.");
            }
        }
        private String getCurrentUsername() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication.getName();
        }
    }

    /**
     * 회원 수정 닉네임 중복 검증
     */
    @Component
    @RequiredArgsConstructor
    public static class UserNicknameModifyValidator extends AbstractValidator<UpdateUserDto> {
        UserRepository userRepository;

        String currentNickname = getCurrentNickname();
        @Override
        protected void doValidate(UpdateUserDto dto, Errors errors) {
            if(!dto.getNickname().equals(currentNickname) && userRepository.existsByNickname(dto.getNickname())) {
                errors.rejectValue("nickname", "닉네임 중복 오류", "이미 사용중인 닉네임 입니다.");
            }
        }
        private String getCurrentNickname() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
                return ((CustomUserDetails) authentication.getPrincipal()).getNickname();
            }
            return null;
        }
    }
}