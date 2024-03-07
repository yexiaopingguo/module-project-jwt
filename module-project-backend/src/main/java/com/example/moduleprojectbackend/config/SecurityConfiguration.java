package com.example.moduleprojectbackend.config;

import com.example.moduleprojectbackend.entity.RestBean;
import com.example.moduleprojectbackend.entity.dto.Account;
import com.example.moduleprojectbackend.entity.vo.response.AuthorizeVO;
import com.example.moduleprojectbackend.filter.JwtAuthorizeFilter;
import com.example.moduleprojectbackend.service.AccountService;
import com.example.moduleprojectbackend.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils utils;

    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;

    @Resource
    AccountService accountService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api/auth/**", "/error").permitAll()
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
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(this::onExceptionHandle)
                        .accessDeniedHandler(this::onAccessDenied)
                )
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用session, 使用jwt登入
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
//                .cors(conf -> {
//                    CorsConfiguration cors = new CorsConfiguration();
//                    //添加前端站点地址，这样就可以告诉浏览器信任了
//                    // cors.addAllowedOrigin("http://localhost:8080");
//                    // 允许所有
//                    cors.addAllowedOriginPattern("*");
//                    //但是这样并不安全，我们应该只许可给我们信任的站点
//                    cors.setAllowCredentials(true);  //允许跨域请求中携带Cookie
//                    cors.addAllowedHeader("*");   //其他的也可以配置，为了方便这里就 * 了
//                    cors.addAllowedMethod("*");
//                    cors.addExposedHeader("*");
//                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//                    source.registerCorsConfiguration("/**", cors);  //直接针对于所有地址生效
//                    conf.configurationSource(source);
//                })
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private void onAuthenticationFailure(HttpServletRequest request,
                                         HttpServletResponse response,
                                         AuthenticationException e) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.failure(401, "登入失败").asJsonString());
    }

    private void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        User user = (User) authentication.getPrincipal();
        Account account = accountService.findAccountByNameOrEmail(user.getUsername());
        String token = utils.createJwt(user, account.getId(), account.getUsername());
        AuthorizeVO vo = new AuthorizeVO();
        vo.setExpire(utils.expireTime());
        vo.setRole(account.getRole());
        vo.setToken(token);
        vo.setUsername(account.getUsername());
        response.getWriter().write(RestBean.success(vo).asJsonString());
    }

    private void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if (utils.invalidateJwt(authorization)) {
            writer.write(RestBean.success().asJsonString());
        } else {
            writer.write(RestBean.failure(400, "退出登入失败").asJsonString());
        }
    }

    private void onExceptionHandle(HttpServletRequest request,
                                 HttpServletResponse response,
                                        AuthenticationException e) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.failure(401, "验证异常").asJsonString());
    }

    private void onAccessDenied(HttpServletRequest request,
                                   HttpServletResponse response,
                                AccessDeniedException e) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.failure(401, "用户权限不足").asJsonString());
    }



}
