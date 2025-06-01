package com.guohenry.matchapp.security;

import com.guohenry.matchapp.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // 建構子注入 JwtUtil 工具
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //每一次 HTTP 請求都會被這個 Filter 檢查一次
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 從 HTTP Header 取得 Authorization 欄位
        String authHeader = request.getHeader("Authorization");

        // 確認 Header存在, 再確認是 Bearer 開頭
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            // 取得 token 本體（去掉 Bearer ）
            String token = authHeader.substring(7);

            try{
                //DI 注入的 jwtUtil 實例來驗證 Token
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractEmail(token);
                    // 加上 ROLE_USER 權限，避免 403 Forbidden
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    Collections.singletonList(() -> "ROLE_USER")
                            );


//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());


                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch (Exception e){
                // 清除上下文中的驗證資訊
                SecurityContextHolder.clearContext();
            }


        }

        // 繼續往後執行（Filter 鏈）
        filterChain.doFilter(request, response);

    }
}
