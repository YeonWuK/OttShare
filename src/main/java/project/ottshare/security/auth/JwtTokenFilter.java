package project.ottshare.security.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.ottshare.service.TokenBlacklistService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (isInvalidToken(token)){ //토큰 검증
            log.info("Invalid token");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtTokenProvider.extractUsername(token);

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            log.info("Incorrect username : {}", username);
            filterChain.doFilter(request, response);
            return;
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
        log.info("successfully logged in");
        if (jwtTokenProvider.isValidateToken(token, customUserDetails.getUsername())) {
            log.info("successfully validate token");
            setAuthentication(customUserDetails, request);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }

    private boolean isInvalidToken(String token) {
        if (token == null) {
            log.debug("Authorization 헤더가 없거나 잘못된 형식입니다.");
            return true;
        }
        if (tokenBlacklistService.isBlacklisted(token)) {
            log.info("토큰이 블랙리스트에 등록되어 있습니다.");
            return true;
        }
        return false;
    }

    private void setAuthentication(CustomUserDetails customUserDetails, HttpServletRequest request) {
        // SecurityContext에 인증 정보 설정
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
