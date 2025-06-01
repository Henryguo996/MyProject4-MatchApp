package com.guohenry.matchapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guohenry.matchapp.model.Member;
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

    public void saveMember(Member member){
        try{
            // Key 前綴
           String key = "login:" + member.getEmail();
            // 序列化成 JSON 字串
           String value = objectMapper.writeValueAsString(member);
           redisTemplate.opsForValue().set(key, value, Duration.ofHours(24)); //存入有效限時24h

        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }

    // 從 Redis 讀取會員資料
    public Member getMember(String email){
        try{
           String key = "login:" + email;
           String json = redisTemplate.opsForValue().get(key);
           if(json != null){
               return objectMapper.readValue(json, Member.class);
           }
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

}
