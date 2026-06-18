package com.example.bonsai_shop.controller.user;

import com.example.bonsai_shop.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ChangePasswordController {

    private final UserService userService;

    @GetMapping("/profile/change-password")
    public String changePasswordPage() {
        return "user/change-password";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {

        try {
            String email = userDetails.getUsername();

            userService.changePassword(
                    email,
                    currentPassword,
                    newPassword,
                    confirmPassword
            );

            model.addAttribute("success", "Đổi mật khẩu thành công!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "user/change-password";
    }
}