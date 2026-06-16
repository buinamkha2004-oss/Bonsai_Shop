package com.example.bonsai_shop.service.user;

import com.example.bonsai_shop.entity.User;
import com.example.bonsai_shop.repository.user.UserRepository;
import com.example.bonsai_shop.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy email: " + email));

        // Chặn user chưa kích hoạt
        if ("PENDING".equals(user.getStatus())) {
            throw new UsernameNotFoundException("Tài khoản chưa được xác thực email!");
        }

        List<SimpleGrantedAuthority> authorities = userRoleRepository.findByUser(user)
                .stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}