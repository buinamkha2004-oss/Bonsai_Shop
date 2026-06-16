package com.example.bonsai_shop.repository.user;

import com.example.bonsai_shop.entity.UserRole;
import com.example.bonsai_shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    List<UserRole> findByUser(User user);
}
