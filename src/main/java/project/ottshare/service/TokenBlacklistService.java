package project.ottshare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void addToBlacklist(String token, long expirationTime) {
        String redisKey = "blacklist_" + token;
        log.info("Adding token to blacklist: {}", redisKey);
        log.info("Expiration time in milliseconds: {}", expirationTime);

        if (expirationTime > 0) {
            redisTemplate.opsForValue().set(redisKey, true, expirationTime, TimeUnit.MILLISECONDS);
            log.info("Token added to blacklist successfully.");
        } else {
            log.warn("Expiration time is invalid. Token not added to blacklist.");
        }
    }

    public boolean isBlacklisted(String token) {
        Boolean isBlacklisted = (Boolean) redisTemplate.opsForValue().get(BLACKLIST_PREFIX + token);
        return isBlacklisted != null && isBlacklisted;
    }
}
