package com.example.bonsai_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`ORDER`")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BonsaiOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Column(name = "OrderCode", nullable = false, unique = true, length = 100)
    private String orderCode;

    @Column(name = "CustomerName", length = 255)
    private String customerName;

    @Column(name = "CustomerPhone", length = 20)
    private String customerPhone;

    @Column(name = "CustomerEmail", length = 255)
    private String customerEmail;

    @Column(name = "ShippingAddress", length = 500)
    private String shippingAddress;

    @Column(name = "OrderDate")
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "TotalAmount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "DepositAmount", precision = 15, scale = 2)
    private BigDecimal depositAmount = BigDecimal.ZERO;

    @Column(name = "OrderStatus", length = 50)
    private String orderStatus = "PENDING";

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;
}