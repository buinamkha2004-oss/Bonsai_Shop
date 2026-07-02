package com.example.bonsai_shop.seller.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
public class SellerMediaStorageService {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif",
            "video/mp4",
            "video/webm"
    );

    @Value("${file.product-media-dir:uploads/products}")
    private String uploadDir;

    public String storeProductMedia(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File không hợp lệ!");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new RuntimeException("Chỉ hỗ trợ ảnh JPG/PNG/WEBP/GIF hoặc video MP4/WEBM!");
        }

        if (file.getSize() > 50L * 1024 * 1024) {
            throw new RuntimeException("Media không được vượt quá 50MB!");
        }

        try {
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String filename = UUID.randomUUID() + extension;
            Files.copy(file.getInputStream(), uploadPath.resolve(filename));
            return "/products/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu media: " + e.getMessage());
        }
    }

    public void deleteProductMedia(String mediaUrl) {
        if (mediaUrl == null || mediaUrl.isBlank()) {
            return;
        }

        try {
            String filename = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1);
            Files.deleteIfExists(Paths.get(uploadDir).resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Không thể xóa media cũ!");
        }
    }
}
