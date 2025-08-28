package com.main.splitxpense.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expense_splits")
public class ExpenseSplit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "split_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal splitAmount;

    @Column(name = "split_percentage", precision = 5, scale = 2)
    private BigDecimal splitPercentage;

    @Column(name = "split_share")
    private Integer splitShare;

    public ExpenseSplit() {}

    public ExpenseSplit(Expense expense, Long userId, BigDecimal splitAmount) {
        this.expense = expense;
        this.userId = userId;
        this.splitAmount = splitAmount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Expense getExpense() { return expense; }
    public void setExpense(Expense expense) { this.expense = expense; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BigDecimal getSplitAmount() { return splitAmount; }
    public void setSplitAmount(BigDecimal splitAmount) { this.splitAmount = splitAmount; }

    public BigDecimal getSplitPercentage() { return splitPercentage; }
    public void setSplitPercentage(BigDecimal splitPercentage) { this.splitPercentage = splitPercentage; }

    public Integer getSplitShare() { return splitShare; }
    public void setSplitShare(Integer splitShare) { this.splitShare = splitShare; }
}
