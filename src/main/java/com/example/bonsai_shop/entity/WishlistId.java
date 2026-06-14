package com.example.bonsai_shop.entity;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class WishlistId implements Serializable {
    private Integer user;
    private Integer product;
}