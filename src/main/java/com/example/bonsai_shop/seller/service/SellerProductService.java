package com.example.bonsai_shop.seller.service;

import com.example.bonsai_shop.customer.repository.UserRepository;
import com.example.bonsai_shop.entity.Product;
import com.example.bonsai_shop.entity.ProductMedia;
import com.example.bonsai_shop.entity.ProductSegment;
import com.example.bonsai_shop.entity.User;
import com.example.bonsai_shop.entity.Variety;
import com.example.bonsai_shop.product.repository.ProductMediaRepository;
import com.example.bonsai_shop.product.repository.ProductRepository;
import com.example.bonsai_shop.product.repository.ProductSegmentRepository;
import com.example.bonsai_shop.product.repository.VarietyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerProductService {

    private final ProductRepository productRepository;
    private final ProductMediaRepository productMediaRepository;
    private final ProductSegmentRepository productSegmentRepository;
    private final VarietyRepository varietyRepository;
    private final UserRepository userRepository;
    private final SellerMediaStorageService mediaStorageService;

    public User getSeller(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy seller!"));
    }

    public List<Product> getMyProducts(String sellerEmail) {
        return productRepository.findBySellerOrderByCreatedAtDesc(getSeller(sellerEmail));
    }

    public Product getMyProduct(String sellerEmail, Integer productId) {
        return productRepository.findByProductIdAndSeller(productId, getSeller(sellerEmail))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm thuộc seller này!"));
    }

    @Transactional
    public Product createProduct(String sellerEmail,
                                 Integer varietyId,
                                 Integer segmentId,
                                 String productCode,
                                 String productName,
                                 String description,
                                 Integer age,
                                 Float height,
                                 Float trunkDiameter,
                                 String style,
                                 BigDecimal price,
                                 Boolean isPublicPrice,
                                 String productStatus) {
        User seller = getSeller(sellerEmail);
        Variety variety = varietyRepository.findById(varietyId)
                .orElseThrow(() -> new RuntimeException("Variety không tồn tại!"));
        ProductSegment segment = productSegmentRepository.findById(segmentId)
                .orElseThrow(() -> new RuntimeException("Segment không tồn tại!"));

        Product product = Product.builder()
                .seller(seller)
                .variety(variety)
                .segment(segment)
                .productCode(productCode)
                .productName(productName)
                .description(description)
                .age(age)
                .height(height)
                .trunkDiameter(trunkDiameter)
                .style(style)
                .price(price)
                .isPublicPrice(Boolean.TRUE.equals(isPublicPrice))
                .productStatus(productStatus == null || productStatus.isBlank() ? "DRAFT" : productStatus)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(String sellerEmail,
                                 Integer productId,
                                 Integer varietyId,
                                 Integer segmentId,
                                 String productCode,
                                 String productName,
                                 String description,
                                 Integer age,
                                 Float height,
                                 Float trunkDiameter,
                                 String style,
                                 BigDecimal price,
                                 Boolean isPublicPrice,
                                 String productStatus) {
        Product product = getMyProduct(sellerEmail, productId);
        Variety variety = varietyRepository.findById(varietyId)
                .orElseThrow(() -> new RuntimeException("Variety không tồn tại!"));
        ProductSegment segment = productSegmentRepository.findById(segmentId)
                .orElseThrow(() -> new RuntimeException("Segment không tồn tại!"));

        product.setVariety(variety);
        product.setSegment(segment);
        product.setProductCode(productCode);
        product.setProductName(productName);
        product.setDescription(description);
        product.setAge(age);
        product.setHeight(height);
        product.setTrunkDiameter(trunkDiameter);
        product.setStyle(style);
        product.setPrice(price);
        product.setIsPublicPrice(Boolean.TRUE.equals(isPublicPrice));
        product.setProductStatus(productStatus);

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(String sellerEmail, Integer productId) {
        Product product = getMyProduct(sellerEmail, productId);
        productMediaRepository.findByProductOrderByDisplayOrderAscMediaIdAsc(product)
                .forEach(media -> mediaStorageService.deleteProductMedia(media.getMediaUrl()));
        productRepository.delete(product);
    }

    public List<ProductMedia> getMedia(Product product) {
        return productMediaRepository.findByProductOrderByDisplayOrderAscMediaIdAsc(product);
    }

    @Transactional
    public void addMedia(String sellerEmail,
                         Integer productId,
                         MultipartFile file,
                         String slotType,
                         String caption,
                         Boolean isThumbnail,
                         Integer displayOrder) {
        Product product = getMyProduct(sellerEmail, productId);
        String mediaUrl = mediaStorageService.storeProductMedia(file);
        String contentType = file.getContentType();
        String mediaType = contentType != null && contentType.startsWith("video/") ? "VIDEO" : "IMAGE";

        if (Boolean.TRUE.equals(isThumbnail)) {
            productMediaRepository.findByProductOrderByDisplayOrderAscMediaIdAsc(product)
                    .forEach(media -> {
                        media.setIsThumbnail(false);
                        productMediaRepository.save(media);
                    });
        }

        ProductMedia media = ProductMedia.builder()
                .product(product)
                .mediaUrl(mediaUrl)
                .mediaType(mediaType)
                .slotType(slotType)
                .caption(caption)
                .isThumbnail(Boolean.TRUE.equals(isThumbnail))
                .displayOrder(displayOrder == null ? 0 : displayOrder)
                .build();

        productMediaRepository.save(media);
    }

    @Transactional
    public void setThumbnail(String sellerEmail, Integer productId, Integer mediaId) {
        Product product = getMyProduct(sellerEmail, productId);
        ProductMedia selected = productMediaRepository.findByMediaIdAndProduct(mediaId, product)
                .orElseThrow(() -> new RuntimeException("Media không tồn tại!"));

        productMediaRepository.findByProductOrderByDisplayOrderAscMediaIdAsc(product)
                .forEach(media -> {
                    media.setIsThumbnail(media.getMediaId().equals(selected.getMediaId()));
                    productMediaRepository.save(media);
                });
    }

    @Transactional
    public void deleteMedia(String sellerEmail, Integer productId, Integer mediaId) {
        Product product = getMyProduct(sellerEmail, productId);
        ProductMedia media = productMediaRepository.findByMediaIdAndProduct(mediaId, product)
                .orElseThrow(() -> new RuntimeException("Media không tồn tại!"));

        mediaStorageService.deleteProductMedia(media.getMediaUrl());
        productMediaRepository.delete(media);
    }

    @Transactional
    public void publish(String sellerEmail, Integer productId) {
        Product product = getMyProduct(sellerEmail, productId);
        if (productMediaRepository.findByProductOrderByDisplayOrderAscMediaIdAsc(product).isEmpty()) {
            throw new RuntimeException("Cần ít nhất một ảnh hoặc video trước khi publish!");
        }
        product.setProductStatus("AVAILABLE");
        productRepository.save(product);
    }

    public List<Variety> getVarieties() {
        return varietyRepository.findAll();
    }

    public List<ProductSegment> getSegments() {
        return productSegmentRepository.findAll();
    }
}
