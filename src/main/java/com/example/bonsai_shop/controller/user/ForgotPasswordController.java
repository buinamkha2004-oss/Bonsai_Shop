package com.example.bonsai_shop.controller.user;

import com.example.bonsai_shop.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final UserService userService;

    // ===== BƯỚC 1: Nhập email =====
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "/user/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String sendOtp(@RequestParam String email, Model model) {
        try {
            userService.sendOtpResetPassword(email);
            model.addAttribute("email", email);
            model.addAttribute("success", "Mã OTP đã được gửi đến " + email);
            return "/user/verify-otp-reset-password";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "/user/forgot-password";
        }
    }

//     ===== BƯỚC 2: Nhập OTP =====

    @PostMapping("/verify-otp-to-reset-password")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otpCode,
                            Model model) {
        try {
            userService.verifyOtp(email, otpCode);
            model.addAttribute("email", email);
            return "/user/reset-password";
        } catch (RuntimeException e) {
            model.addAttribute("email", email);
            model.addAttribute("error", e.getMessage());
            return "/user/verify-otp-reset-password";
        }
    }
    @PostMapping("/resend-otp-reset")
    public String resendOtpReset(@RequestParam String email, Model model) {
        try {
            userService.sendOtp(email);
            model.addAttribute("email", email);
            model.addAttribute("success", "Mã OTP mới đã được gửi đến " + email);
            return "/user/verify-otp-reset-password";
        } catch (RuntimeException e) {
            model.addAttribute("email", email);
            model.addAttribute("error", e.getMessage());
            return "/user/verify-otp-reset-password";
        }
    }

    // ===== BƯỚC 3: Đặt lại mật khẩu =====
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("email", email);
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "/user/reset-password";
        }
        try {
            userService.resetPassword(email, newPassword);
            model.addAttribute("success", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập.");
            return "/user/login";
        } catch (RuntimeException e) {
            model.addAttribute("email", email);
            model.addAttribute("error", e.getMessage());
            return "/user/reset-password";
        }
    }
}