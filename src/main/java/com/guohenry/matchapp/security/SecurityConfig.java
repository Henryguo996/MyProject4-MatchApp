package com.guohenry.matchapp.security;

import com.guohenry.matchapp.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        // 使用CSRF
//        http
//                .csrf(csrf -> csrf.disable()) // 關閉 CSRF 保護（讓 POST 可測）
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()  // 所有請求都開放
//                );
//        return http.build();
//    }

        // 禁用 CSRF、Session，改用 JWT 驗證方式
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 權限設定（可自由調整）
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/member/login", "/api/member/register", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated() // 其餘 API 需要 JWT 驗證
                )

        //  // JwtUtil 傳給自訂的 Filter
       .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}