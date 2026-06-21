package com.example.bonsai_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "ROLE_ACTION")
@IdClass(RoleActionId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoleAction implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "RoleID", nullable = false)
    private Role role;

    @Id
    @ManyToOne
    @JoinColumn(name = "ActionID", nullable = false)
    private BusinessAction action;

    @Column(name = "IsEnabled")
    private Boolean isEnabled = true;
}