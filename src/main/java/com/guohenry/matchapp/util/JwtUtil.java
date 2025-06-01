package com.guohenry.matchapp.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 秘密金鑰，用於簽名 JWT（實務上建議寫在 application.properties 並加密儲存）
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // JWT 的存活時間（例如 24 小時 = 86400000 毫秒）
    private final long expiration = 1000 * 60 * 60 * 24;

    //產生 JWT (輸入會員 email 作為 subject)
    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)                    // 主體資料
                .setIssuedAt(new Date())             // 發行時間
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 過期時間
                .signWith(key)                       // 使用秘密金鑰簽章
                .compact();                          // 組合成字串
    }

    // 驗證 JWT 是否有效（簽章正確、未過期）
    public String extractEmail(String token){
        return parseToken(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        try{
            parseToken(token);
            return true;

        }catch (JwtException | IllegalArgumentException e){
            return false;

        }
    }

    // 解析 Token 的通用方法（內部用）
    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)    // 指定簽章密鑰
                .build()
                .parseClaimsJws(token); // 解出 Token（並驗證簽章與格式）
    }


}
