package com.example.bonsai_shop.controller;

import com.example.bonsai_shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // ===== TRANG LOGIN =====
    @GetMapping("/login")
    public String loginPage() {
        return "/login"; // templates/login.html
    }

    // ===== TRANG REGISTER =====
    @GetMapping("/register")
    public String registerPage() {
        return "/register"; // templates/register.html
    }

    // ===== XỬ LÝ REGISTER =====
    @PostMapping("/register")
    public String register(@RequestParam String fullName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String phone,
                           RedirectAttributes redirectAttributes) {
        try {
            userService.register(fullName, email, password, phone);
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}