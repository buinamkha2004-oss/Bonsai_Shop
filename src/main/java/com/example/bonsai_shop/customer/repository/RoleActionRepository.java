package com.example.bonsai_shop.customer.repository;

import com.example.bonsai_shop.entity.RoleAction;
import com.example.bonsai_shop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoleActionRepository extends JpaRepository<RoleAction, Integer> {
    List<RoleAction> findByRole(Role role);
    List<RoleAction> findByRoleRoleIdAndIsEnabledTrue(Integer roleId);
}