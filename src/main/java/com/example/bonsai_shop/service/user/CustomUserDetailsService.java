package com.example.bonsai_shop.service.user;

import com.example.bonsai_shop.entity.User;
import com.example.bonsai_shop.repository.user.RoleActionRepository;
import com.example.bonsai_shop.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleActionRepository roleActionRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy email: " + email));

        if ("PENDING".equals(user.getStatus())) {
            throw new UsernameNotFoundException("Tài khoản chưa được xác thực email!");
        }
        // 1. Authority cho Role (dùng cho hasRole())
        SimpleGrantedAuthority roleAuthority =
                new SimpleGrantedAuthority(user.getRole().getRoleName());

        // 2. Authority cho từng Action cụ thể (dùng cho hasAuthority())
        List<SimpleGrantedAuthority> actionAuthorities = roleActionRepository
                .findByRoleRoleIdAndIsEnabledTrue(user.getRole().getRoleId())
                .stream()
                .map(roleAction -> new SimpleGrantedAuthority(
                        "ACTION_" + roleAction.getAction().getActionCode()))
                .toList();

        // Gộp cả 2 loại authority lại
        List<SimpleGrantedAuthority> allAuthorities = Stream
                .concat(Stream.of(roleAuthority), actionAuthorities.stream())
                .toList();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                allAuthorities
        );
    }


}