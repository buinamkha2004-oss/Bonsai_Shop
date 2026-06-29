package com.example.bonsai_shop.service.product;

import com.example.bonsai_shop.dto.ProductCardDTO;
import com.example.bonsai_shop.entity.Product;
import com.example.bonsai_shop.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<ProductCardDTO> getMarketplaceProducts(Pageable pageable) {
        return productRepository.findMarketplaceProducts(pageable);
    }

    public Page<Product> getAllActiveProducts(Pageable pageable) {
        return productRepository.findAllActiveProducts(pageable);
    }
}
