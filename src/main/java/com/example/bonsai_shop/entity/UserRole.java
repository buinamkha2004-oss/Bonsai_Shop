package com.example.bonsai_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_ROLE")
@IdClass(UserRoleId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserRole implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "RoleID", nullable = false)
    private Role role;

    @Column(name = "AssignedAt")
    private LocalDateTime assignedAt = LocalDateTime.now();
}
