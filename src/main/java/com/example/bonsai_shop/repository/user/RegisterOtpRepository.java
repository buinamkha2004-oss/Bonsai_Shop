package com.example.bonsai_shop.repository.user;

import com.example.bonsai_shop.entity.PasswordResetOtp;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RegisterOtpRepository extends JpaRepository<PasswordResetOtp, Integer> {
    Optional<PasswordResetOtp> findTopByEmailOrderByCreatedAtDesc(String email);


    @Transactional
    void deleteByEmail(String email);
}