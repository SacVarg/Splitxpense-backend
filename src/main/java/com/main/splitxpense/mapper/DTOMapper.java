package com.main.splitxpense.mapper;

import com.main.splitxpense.dto.*;
import com.main.splitxpense.model.*;
import com.main.splitxpense.repository.UserRepository;
import com.main.splitxpense.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    // User mapping
    public UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }

    public List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream().map(this::toUserResponse).collect(Collectors.toList());
    }

    // Group mapping
    public GroupResponse toGroupResponse(Group group) {
        GroupResponse response = new GroupResponse();
        response.setId(group.getId());
        response.setName(group.getName());
        response.setCreatedBy(group.getCreatedBy());
        response.setCreatedAt(group.getCreatedAt());

        // Get creator name
        userRepository.findById(group.getCreatedBy())
                .ifPresent(creator -> response.setCreatedByName(creator.getName()));

        // Map members
        if (group.getMembers() != null) {
            response.setMembers(group.getMembers().stream()
                    .map(this::toGroupMemberResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    public List<GroupResponse> toGroupResponseList(List<Group> groups) {
        return groups.stream().map(this::toGroupResponse).collect(Collectors.toList());
    }

    public GroupMemberResponse toGroupMemberResponse(GroupMember member) {
        GroupMemberResponse response = new GroupMemberResponse();
        response.setId(member.getId());
        response.setUserId(member.getUser().getId());
        response.setUserName(member.getUser().getName());
        response.setUserEmail(member.getUser().getEmail());
        response.setUserPhone(member.getUser().getPhone());
        return response;
    }

    // Expense mapping
    public ExpenseResponse toExpenseResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setGroupId(expense.getGroupId());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setPaidBy(expense.getPaidBy());
        response.setSplitType(expense.getSplitType());
        response.setCreatedAt(expense.getCreatedAt());

        // Get payer name
        userRepository.findById(expense.getPaidBy())
                .ifPresent(payer -> response.setPaidByName(payer.getName()));

        // Get group name
        if (expense.getGroupId() != null) {
            groupRepository.findById(expense.getGroupId())
                    .ifPresent(group -> response.setGroupName(group.getName()));
        }

        // Map splits
        if (expense.getSplits() != null) {
            response.setSplits(expense.getSplits().stream()
                    .map(this::toExpenseSplitResponse)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    public List<ExpenseResponse> toExpenseResponseList(List<Expense> expenses) {
        return expenses.stream().map(this::toExpenseResponse).collect(Collectors.toList());
    }

    public ExpenseSplitResponse toExpenseSplitResponse(ExpenseSplit split) {
        ExpenseSplitResponse response = new ExpenseSplitResponse();
        response.setId(split.getId());
        response.setUserId(split.getUserId());
        response.setSplitAmount(split.getSplitAmount());
        response.setSplitPercentage(split.getSplitPercentage());
        response.setSplitShare(split.getSplitShare());

        // Get user name
        userRepository.findById(split.getUserId())
                .ifPresent(user -> response.setUserName(user.getName()));

        return response;
    }

    // Balance mapping
    public BalanceResponse toBalanceResponse(Balance balance) {
        BalanceResponse response = new BalanceResponse();
        response.setId(balance.getId());
        response.setUserId(balance.getUserId());
        response.setGroupId(balance.getGroupId());
        response.setOwesToUserId(balance.getOwesToUserId());
        response.setAmount(balance.getAmount());

        // Set balance type
        response.setBalanceType(balance.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0 ? "owed" : "owes");

        // Get user names
        userRepository.findById(balance.getUserId())
                .ifPresent(user -> response.setUserName(user.getName()));

        userRepository.findById(balance.getOwesToUserId())
                .ifPresent(user -> response.setOwesToUserName(user.getName()));

        // Get group name
        if (balance.getGroupId() != null) {
            groupRepository.findById(balance.getGroupId())
                    .ifPresent(group -> response.setGroupName(group.getName()));
        }

        return response;
    }

    public List<BalanceResponse> toBalanceResponseList(List<Balance> balances) {
        return balances.stream().map(this::toBalanceResponse).collect(Collectors.toList());
    }
}
