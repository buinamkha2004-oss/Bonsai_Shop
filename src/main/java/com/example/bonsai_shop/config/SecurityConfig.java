package com.example.bonsai_shop.config;

import com.example.bonsai_shop.customer.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
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
                        // Trang cÃ´ng khai
                        .requestMatchers(
                                "/",
                                "/products/**",
                                "/register",
                                "/login",
                                "/forgot-password",
                                "/verify-otp",
                                "/resend-otp",
                                "/reset-password",
                                "/verify-otp-to-reset-password",
                                "/resend-otp-reset",
                                "/avatars/**",  // â† cho phÃ©p xem áº£nh avatar
                                "/css/**",      // â† cho phÃ©p CSS
                                "/js/**",       // â† cho phÃ©p JS
                                "/images/**"    // â† cho phÃ©p images
                        ).permitAll()
                        // Chá»‰ ADMIN má»›i vÃ o Ä‘Æ°á»£c /admin/**
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/seller/**").hasAnyRole("SELLER", "ADMIN")
                        // Cháº·n theo Action cá»¥ thá»ƒ (permission-based)
                        .requestMatchers("/products/create", "/products/edit/**", "/products/delete/**")
                                        .hasAuthority("ACTION_PRODUCT_MANAGE")
                        .requestMatchers("/orders/all")
                                         .hasAuthority("ACTION_ORDER_VIEW_ALL")
                        .requestMatchers("/orders/handle-claim/**")
                                         .hasAuthority("ACTION_ORDER_HANDLE_CLAIM")
                        .requestMatchers("/users/**")
                                         .hasAuthority("ACTION_USER_MANAGE")
                        // CÃ¡c trang khÃ¡c cáº§n Ä‘Äƒng nháº­p
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")           // trang login tá»± táº¡o
                        .loginProcessingUrl("/login")  // URL xá»­ lÃ½ form login
                        .successHandler(roleBasedSuccessHandler())
                        .failureUrl("/login?error")    // login sai vá» trang nÃ y
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)      // xÃ³a session
                        .clearAuthentication(true)        // xÃ³a authentication
                        .deleteCookies("JSESSIONID")      // xÃ³a cookie
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler roleBasedSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
            boolean isSeller = authentication.getAuthorities().stream()
                    .anyMatch(authority -> "ROLE_SELLER".equals(authority.getAuthority()));

            if (isAdmin) {
                response.sendRedirect("/admin/users");
            } else if (isSeller) {
                response.sendRedirect("/seller");
            } else {
                response.sendRedirect("/home");
            }
        };
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

