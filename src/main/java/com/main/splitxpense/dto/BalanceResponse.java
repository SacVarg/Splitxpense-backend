package com.main.splitxpense.dto;

import java.math.BigDecimal;

public class BalanceResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long groupId;
    private String groupName;
    private Long owesToUserId;
    private String owesToUserName;
    private BigDecimal amount;
    private String balanceType; // "owes" or "owed"

    public BalanceResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public Long getOwesToUserId() { return owesToUserId; }
    public void setOwesToUserId(Long owesToUserId) { this.owesToUserId = owesToUserId; }

    public String getOwesToUserName() { return owesToUserName; }
    public void setOwesToUserName(String owesToUserName) { this.owesToUserName = owesToUserName; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getBalanceType() { return balanceType; }
    public void setBalanceType(String balanceType) { this.balanceType = balanceType; }
}
