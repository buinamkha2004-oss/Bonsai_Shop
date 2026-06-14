package com.example.bonsai_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "WISHLIST")
@IdClass(WishlistId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Wishlist implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false)
    private Product product;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt = LocalDateTime.now();
}