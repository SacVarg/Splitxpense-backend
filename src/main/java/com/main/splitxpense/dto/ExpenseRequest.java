package com.main.splitxpense.dto;

import com.main.splitxpense.model.Expense.SplitType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ExpenseRequest {
    private Long groupId;

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 255, message = "Description must be between 2 and 255 characters")
    private String description;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Amount must have at most 10 digits before decimal and 2 after")
    private BigDecimal amount;

    @NotNull(message = "Paid by user ID is required")
    private Long paidBy;

    @NotNull(message = "Split type is required")
    private SplitType splitType;

    private List<Long> participants;
    private Map<Long, BigDecimal> percentages;
    private Map<Long, Integer> shares;
    private Map<Long, BigDecimal> exactAmounts;

    public ExpenseRequest() {}

    // Getters and Setters
    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Long getPaidBy() { return paidBy; }
    public void setPaidBy(Long paidBy) { this.paidBy = paidBy; }

    public SplitType getSplitType() { return splitType; }
    public void setSplitType(SplitType splitType) { this.splitType = splitType; }

    public List<Long> getParticipants() { return participants; }
    public void setParticipants(List<Long> participants) { this.participants = participants; }

    public Map<Long, BigDecimal> getPercentages() { return percentages; }
    public void setPercentages(Map<Long, BigDecimal> percentages) { this.percentages = percentages; }

    public Map<Long, Integer> getShares() { return shares; }
    public void setShares(Map<Long, Integer> shares) { this.shares = shares; }

    public Map<Long, BigDecimal> getExactAmounts() { return exactAmounts; }
    public void setExactAmounts(Map<Long, BigDecimal> exactAmounts) { this.exactAmounts = exactAmounts; }
}
