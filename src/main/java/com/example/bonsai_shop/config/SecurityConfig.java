package com.example.bonsai_shop.config;

import com.example.bonsai_shop.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Trang công khai
                        .requestMatchers(
                                "/",
                                "/products/**",
                                "/register",
                                "/login",
                                "/static/css/**",      // ← cho phép CSS
                                "/js/**",       // ← cho phép JS
                                "/images/**"    // ← cho phép images
                        ).permitAll()
                        // Chỉ ADMIN mới vào được /admin/**
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Các trang khác cần đăng nhập
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")           // trang login tự tạo
                        .loginProcessingUrl("/login")  // URL xử lý form login
                        .defaultSuccessUrl("/home", true)  // sau login về trang chủ
                        .failureUrl("/login?error")    // login sai về trang này
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)      // xóa session
                        .clearAuthentication(true)        // xóa authentication
                        .deleteCookies("JSESSIONID")      // xóa cookie
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }
}