package com.example.bonsai_shop.entity;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class ProductTagId implements Serializable {
    private Integer product;
    private Integer tag;
}