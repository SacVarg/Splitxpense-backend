package com.main.splitxpense.dto;

import java.time.LocalDateTime;
import java.util.List;

public class GroupResponse {
    private Long id;
    private String name;
    private Long createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
    private List<GroupMemberResponse> members;

    public GroupResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<GroupMemberResponse> getMembers() { return members; }
    public void setMembers(List<GroupMemberResponse> members) { this.members = members; }
}
