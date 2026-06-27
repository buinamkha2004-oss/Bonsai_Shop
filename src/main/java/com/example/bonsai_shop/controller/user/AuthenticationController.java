package com.example.bonsai_shop.controller.user;

import com.example.bonsai_shop.entity.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;

@PreAuthorize("hasRole('ROLE_ADMIN') or hasAuthority('ACTION_USER_MANAGE')")

public class AuthenticationController {
    public String managerUser(User user) {
        return "managerUser";
    }
}
