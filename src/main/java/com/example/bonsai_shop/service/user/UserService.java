package com.example.bonsai_shop.service.user;

import com.example.bonsai_shop.entity.PasswordResetOtp;
import com.example.bonsai_shop.entity.Role;
import com.example.bonsai_shop.entity.User;
import com.example.bonsai_shop.entity.UserRole;
import com.example.bonsai_shop.repository.user.RegisterOtpRepository;
import com.example.bonsai_shop.repository.user.RoleRepository;
import com.example.bonsai_shop.repository.user.UserRepository;
import com.example.bonsai_shop.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    // ===== ĐĂNG KÝ =====
    @Transactional
    public void register(String fullName, String username, String email, String password, String phone) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // Lưu user với status PENDING
        User user = User.builder()
                .fullName(fullName)
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .phone(phone)
                .status("PENDING") // ← chưa kích hoạt
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        // Gán role USER
        Role role = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role không tồn tại!"));
        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .assignedAt(LocalDateTime.now())
                .build();
        userRoleRepository.save(userRole);

        // Gửi OTP xác thực email
        sendOtp(email);
    }

    // Kích hoạt tài khoản sau khi xác thực OTP
    public void activateUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user!"));
        user.setStatus("ACTIVE");
        userRepository.save(user);
    }

    // ===== LẤY USER THEO EMAIL =====
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user!"));
    }

    private final RegisterOtpRepository otpRepository;
    private final EmailService emailService;

    // ===== GỬI OTP Để Đăng Ký=====
    @Transactional
    public void sendOtp(String email) {
        // Kiểm tra email tồn tại
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống!"));

        // Xóa OTP cũ
        otpRepository.deleteByEmail(email);

        // Tạo OTP 6 số
        String otpCode = String.format("%06d", new java.util.Random().nextInt(999999));

        // Lưu OTP vào database
        PasswordResetOtp otp = PasswordResetOtp.builder()
                .email(email)
                .otpCode(otpCode)
                .expiredAt(LocalDateTime.now().plusMinutes(5)) // hết hạn sau 5 phút
                .isUsed(false)
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(otp);

        // Gửi email
        emailService.sendOtpEmail(email, otpCode);
    }

    //    GỬI OTP để đặt lại mật khẩu
    @Transactional
    public void sendOtpResetPassword(String email) {
        // Kiểm tra email tồn tại
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống!"));

        // Xóa OTP cũ
        otpRepository.deleteByEmail(email);

        // Tạo OTP 6 số
        String otpCode = String.format("%06d", new java.util.Random().nextInt(999999));

        // Lưu OTP vào database
        PasswordResetOtp otp = PasswordResetOtp.builder()
                .email(email)
                .otpCode(otpCode)
                .expiredAt(LocalDateTime.now().plusMinutes(5)) // hết hạn sau 5 phút
                .isUsed(false)
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(otp);

        // Gửi email
        emailService.sendOtpResetPassword(email, otpCode);
    }

    // ===== XÁC NHẬN OTP =====
    public void verifyOtp(String email, String otpCode) {
        PasswordResetOtp otp = otpRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP không tồn tại!"));

        if (otp.getIsUsed()) {
            throw new RuntimeException("OTP đã được sử dụng!");
        }
        if (otp.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP đã hết hạn!");
        }
        if (!otp.getOtpCode().equals(otpCode)) {
            throw new RuntimeException("OTP không đúng!");
        }

        // Đánh dấu OTP đã dùng
        otp.setIsUsed(true);
        otpRepository.save(otp);
    }


    // ===== ĐẶT LẠI MẬT KHẨU =====
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Xóa OTP sau khi đổi mật khẩu xong
        otpRepository.deleteByEmail(email);
    }

    public User getCurrentUserProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user!"));
    }

    private final FileStorageService fileStorageService;

    @Transactional
    public void updateUserProfile(String email, String fullName, String username, String phone,
                                  String address, String avatar, MultipartFile avatarFile) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user!"));

        // Chỉ cập nhật field nào có giá trị (không null)
        if (fullName != null) {
            user.setFullName(fullName);
        }
        if (username != null) {
            user.setUsername(username);
        }
        if (phone != null) {
            user.setPhone(phone);
        }
        if (address != null) {
            user.setAddress(address);
        }

        // Chỉ xử lý avatar nếu có file thực sự được upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarPath = fileStorageService.storeAvatar(avatarFile);
            user.setAvatar(avatarPath);
        }

        userRepository.save(user);
    }

    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword, String confirmPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng!");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp!");
        }

        if (newPassword.length() < 6) {
            throw new RuntimeException("Mật khẩu phải có ít nhất 6 ký tự!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
