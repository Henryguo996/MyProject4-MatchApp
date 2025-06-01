package com.guohenry.matchapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 將物件轉成 JSON 字串後存入 Redis
    public void save(String key, Object value, long timeoutSeconds){
        try{
            String json = objectMapper.writeValueAsString(value);

            redisTemplate.opsForValue().set(key, json, Duration.ofSeconds(timeoutSeconds));

        }catch(JsonProcessingException e){
            throw new RuntimeException("轉換 JSON 失敗!", e);
        }
    }

    // 從 Redis 讀取 JSON 字串並轉回物件
    public <T> T get(String key, Class<T> clazz){
        try{
            String json = redisTemplate.opsForValue().get(key);
            if(json != null){
                return objectMapper.readValue(json, clazz);
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException("讀取 Redis 失敗!", e);
        }
    }

    // 刪除 Redis key
    public void delete(String key){
        redisTemplate.delete(key);
    }


}
