package com.example.bonsai_shop.repository.user;

import com.example.bonsai_shop.entity.BusinessAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BusinessActionRepository extends JpaRepository<BusinessAction, Integer> {
    Optional<BusinessAction> findByActionCode(String actionCode);
}
