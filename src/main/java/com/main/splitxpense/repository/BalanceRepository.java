package com.main.splitxpense.repository;

import com.main.splitxpense.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    List<Balance> findByUserId(Long userId);
    List<Balance> findByGroupId(Long groupId);
    List<Balance> findByUserIdAndOwesToUserId(Long userId, Long owesToUserId);
}
