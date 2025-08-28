package com.main.splitxpense.dto;

import java.math.BigDecimal;

public class ExpenseSplitResponse {
    private Long id;
    private Long userId;
    private String userName;
    private BigDecimal splitAmount;
    private BigDecimal splitPercentage;
    private Integer splitShare;

    public ExpenseSplitResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public BigDecimal getSplitAmount() { return splitAmount; }
    public void setSplitAmount(BigDecimal splitAmount) { this.splitAmount = splitAmount; }

    public BigDecimal getSplitPercentage() { return splitPercentage; }
    public void setSplitPercentage(BigDecimal splitPercentage) { this.splitPercentage = splitPercentage; }

    public Integer getSplitShare() { return splitShare; }
    public void setSplitShare(Integer splitShare) { this.splitShare = splitShare; }
}
