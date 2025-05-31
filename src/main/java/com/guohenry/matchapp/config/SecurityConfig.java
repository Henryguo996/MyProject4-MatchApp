package com.guohenry.matchapp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 關閉 CSRF 保護（讓 POST 可測）
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 所有請求都開放
                );
        return http.build();
    }
}