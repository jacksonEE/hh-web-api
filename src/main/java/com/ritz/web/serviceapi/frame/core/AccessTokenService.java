package com.ritz.web.serviceapi.frame.core;


import com.ritz.web.serviceapi.frame.RedisKeyConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class AccessTokenService {

    @Autowired
    public RedisTemplate<String, Object> redisTemplate;

    public String save(Integer userId, UserType userType) {
        try {
            boolean isManager = userType == UserType.MANAGER;
            String key = getTokenMapKey(isManager);
            HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
            Object o = hashOperations.entries(key).get(userId.toString());
            boolean refresh = o != null;
            Token token;
            String tokenId;
            if (refresh) {
                tokenId = o.toString();
                token = find(tokenId);
                if (token == null) {
                    redisTemplate.opsForHash().delete(key, userId.toString());
                    return saveToken(userId, userType, key);
                } else {
                    token.setLoginTime(new Date());
                    redisTemplate.opsForValue().set(tokenId, token);
                }
                return tokenId;
            } else {
                return saveToken(userId, userType, key);
            }
        } catch (Exception e) {
            log.error("fail to save token:", e);
        }
        return null;
    }

    Token find(String tokenId) {
        Object o = null;
        try {
            o = redisTemplate.opsForValue().get(tokenId);
        } catch (Exception e) {
            log.error("fail to get token:", e);
        }
        return o == null ? null : (Token) o;
    }

    void remove(String tokenId) {
        try {
            redisTemplate.delete(tokenId);
        } catch (Exception e) {
            log.error("fail to remove token - {}:", tokenId, e);
        }
    }

    private String tokenId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String getTokenMapKey(boolean isManager) {
        return isManager ? RedisKeyConstant.MANAGER_LOGIN_MAP
                : RedisKeyConstant.USER_LOGIN_MAP;
    }

    private String saveToken(Integer userId, UserType userType, String mapKey) {
        String tokenId;
        Token token;
        tokenId = tokenId();
        token = new Token(userId, userType, new Date());
        redisTemplate.opsForValue().set(tokenId, token);
        redisTemplate.opsForHash().put(mapKey, userId.toString(), tokenId);
        return tokenId;
    }
}
