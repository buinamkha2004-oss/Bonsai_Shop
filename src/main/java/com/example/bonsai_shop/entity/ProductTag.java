package com.example.bonsai_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "PRODUCT_TAG")
@IdClass(ProductTagId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductTag implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false)
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "TagID", nullable = false)
    private Tag tag;
}