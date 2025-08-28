package com.main.splitxpense.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "balances")
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "owes_to_user_id", nullable = false)
    private Long owesToUserId;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    public Balance() {}

    public Balance(Long userId, Long groupId, Long owesToUserId, BigDecimal amount) {
        this.userId = userId;
        this.groupId = groupId;
        this.owesToUserId = owesToUserId;
        this.amount = amount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public Long getOwesToUserId() { return owesToUserId; }
    public void setOwesToUserId(Long owesToUserId) { this.owesToUserId = owesToUserId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
