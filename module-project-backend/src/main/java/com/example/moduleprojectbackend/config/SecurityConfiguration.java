package com.example.moduleprojectbackend.config;

import com.example.moduleprojectbackend.entity.RestBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .failureHandler(this::onAuthenticationFailure)
                        .successHandler(this::onAuthenticationSuccess)
                        .permitAll()
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }

    private void onAuthenticationFailure(HttpServletRequest request,
                                         HttpServletResponse response,
                                         AuthenticationException e) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.failure(401, "登入失败").asJsonString());
    }

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.success("登入成功").asJsonString());
    }

    private void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.getWriter().write("Logout Success");
    }

}
