package com.example.bonsai_shop.controller.product;

import com.example.bonsai_shop.dto.ProductCardDTO;
import com.example.bonsai_shop.entity.Product;
import com.example.bonsai_shop.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MarketplaceController {
    private final ProductService productService;

    @GetMapping("/marketplace")
    public String marketplace(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "availableOnly", required = false) String availableOnly,
            Model model) {

        boolean showAvailableOnly = "on".equals(availableOnly) || "true".equals(availableOnly);

        Page<Product> products;
        if (showAvailableOnly) {
            products = productService.getAvailableProductsOnly(
                    PageRequest.of(page, 12));
        } else {
            products = productService.getAllActiveProducts(
                    PageRequest.of(page, 12));
        }

        model.addAttribute("products", products);
        model.addAttribute("availableOnly", showAvailableOnly);
        return "/product/marketplace";
    }
}