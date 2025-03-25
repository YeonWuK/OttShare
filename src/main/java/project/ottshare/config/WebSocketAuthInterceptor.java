package project.ottshare.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import project.ottshare.security.auth.JwtTokenProvider;

import java.util.Map;

@Component
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public WebSocketAuthInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("🔍 WebSocketAuthInterceptor - beforeHandshake 실행됨");

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            // ✅ Query Parameter에서 JWT 토큰 가져오기
            String token = httpRequest.getParameter("token");
            log.info("🔑 WebSocket 요청의 JWT 토큰: {}", token);

            if (token == null || !jwtTokenProvider.validateToken(token)) {
                log.error("❌ 유효하지 않은 JWT 토큰");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;  // 401 Unauthorized 반환
            }

            // ✅ JWT 토큰이 유효하면 사용자 정보 저장
            attributes.put("user", jwtTokenProvider.extractUsername(token));
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        log.info("✅ WebSocketAuthInterceptor - afterHandshake 실행됨");
    }
}