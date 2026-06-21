package com.example.bonsai_shop.entity;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class RoleActionId implements Serializable {
    private Integer role;
    private Integer action;
}