package com.example.bonsai_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "PRODUCT_SEGMENT")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SegmentID")
    private Integer segmentId;

    @Column(name = "SegmentName", nullable = false, length = 255)
    private String segmentName;

    @OneToMany(mappedBy = "segment", cascade = CascadeType.ALL)
    private List<Product> products;
}