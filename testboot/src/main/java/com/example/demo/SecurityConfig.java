package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // すべてのリクエスト（URL）へのアクセスを許可する
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // デフォルトのログインフォームを無効化
            .formLogin(form -> form.disable())
            // CSRF対策を一旦無効化（フォーム送信時の403エラー防止）
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}