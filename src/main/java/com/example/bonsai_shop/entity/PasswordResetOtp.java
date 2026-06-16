package com.example.bonsai_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PASSWORD_RESET_OTP")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PasswordResetOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OtpID")
    private Integer otpId;

    @Column(name = "Email", nullable = false, length = 255)
    private String email;

    @Column(name = "OtpCode", nullable = false, length = 6)
    private String otpCode;

    @Column(name = "ExpiredAt", nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "IsUsed")
    private Boolean isUsed = false;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt = LocalDateTime.now();
}