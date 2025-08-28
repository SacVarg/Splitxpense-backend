package com.main.splitxpense.service;

import com.main.splitxpense.dto.ExpenseRequest;
import com.main.splitxpense.dto.ExpenseResponse;
import com.main.splitxpense.dto.BalanceResponse;
import com.main.splitxpense.exception.*;
import com.main.splitxpense.mapper.DTOMapper;
import com.main.splitxpense.model.*;
import com.main.splitxpense.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private DTOMapper dtoMapper;

    public ExpenseResponse createExpense(ExpenseRequest request) {
        // Validate the request
        validateExpenseRequest(request);

        Expense expense = new Expense();
        expense.setGroupId(request.getGroupId());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setPaidBy(request.getPaidBy());
        expense.setSplitType(request.getSplitType());

        Expense savedExpense = expenseRepository.save(expense);

        List<ExpenseSplit> splits = calculateSplits(savedExpense, request);
        for (ExpenseSplit split : splits) {
            split.setExpense(savedExpense);
            expenseSplitRepository.save(split);
        }

        savedExpense.setSplits(splits);
        updateBalances(savedExpense);

        Expense reloadedExpense = expenseRepository.findById(savedExpense.getId()).orElse(savedExpense);
        return dtoMapper.toExpenseResponse(reloadedExpense);
    }

    private void validateExpenseRequest(ExpenseRequest request) {
        // Validate payer exists
        if (!userRepository.existsById(request.getPaidBy())) {
            throw new UserNotFoundException(request.getPaidBy());
        }

        // Validate group exists (if provided)
        if (request.getGroupId() != null && !groupRepository.existsById(request.getGroupId())) {
            throw new GroupNotFoundException(request.getGroupId());
        }

        // Validate split-specific data
        switch (request.getSplitType()) {
            case EQUAL:
                validateEqualSplit(request);
                break;
            case PERCENTAGE:
                validatePercentageSplit(request);
                break;
            case SHARE:
                validateShareSplit(request);
                break;
            case EXACT:
                validateExactSplit(request);
                break;
        }
    }

    private void validateEqualSplit(ExpenseRequest request) {
        if (request.getParticipants() == null || request.getParticipants().isEmpty()) {
            throw InvalidSplitException.emptyParticipants();
        }

        // Validate all participants exist
        for (Long userId : request.getParticipants()) {
            if (!userRepository.existsById(userId)) {
                throw new UserNotFoundException(userId);
            }
        }
    }

    private void validatePercentageSplit(ExpenseRequest request) {
        if (request.getPercentages() == null || request.getPercentages().isEmpty()) {
            throw InvalidSplitException.emptyParticipants();
        }

        BigDecimal totalPercentage = BigDecimal.ZERO;
        for (Map.Entry<Long, BigDecimal> entry : request.getPercentages().entrySet()) {
            // Validate user exists
            if (!userRepository.existsById(entry.getKey())) {
                throw new UserNotFoundException(entry.getKey());
            }

            // Validate percentage is positive
            if (entry.getValue().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidSplitException("Percentage values must be positive");
            }

            totalPercentage = totalPercentage.add(entry.getValue());
        }

        // Check if percentages add up to 100%
        if (totalPercentage.compareTo(new BigDecimal("100")) != 0) {
            throw InvalidSplitException.percentageNotAddUpTo100(totalPercentage);
        }
    }

    private void validateShareSplit(ExpenseRequest request) {
        if (request.getShares() == null || request.getShares().isEmpty()) {
            throw InvalidSplitException.emptyParticipants();
        }

        for (Map.Entry<Long, Integer> entry : request.getShares().entrySet()) {
            // Validate user exists
            if (!userRepository.existsById(entry.getKey())) {
                throw new UserNotFoundException(entry.getKey());
            }

            // Validate share is positive
            if (entry.getValue() <= 0) {
                throw InvalidSplitException.invalidShareValue();
            }
        }
    }

    private void validateExactSplit(ExpenseRequest request) {
        if (request.getExactAmounts() == null || request.getExactAmounts().isEmpty()) {
            throw InvalidSplitException.emptyParticipants();
        }

        BigDecimal totalSplitAmount = BigDecimal.ZERO;
        for (Map.Entry<Long, BigDecimal> entry : request.getExactAmounts().entrySet()) {
            // Validate user exists
            if (!userRepository.existsById(entry.getKey())) {
                throw new UserNotFoundException(entry.getKey());
            }

            // Validate amount is positive
            if (entry.getValue().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidSplitException("Exact amounts must be positive");
            }

            totalSplitAmount = totalSplitAmount.add(entry.getValue());
        }

        // Check if exact amounts match expense amount
        if (totalSplitAmount.compareTo(request.getAmount()) != 0) {
            throw InvalidSplitException.exactAmountMismatch(request.getAmount(), totalSplitAmount);
        }
    }

    public List<ExpenseResponse> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return dtoMapper.toExpenseResponseList(expenses);
    }

    public List<ExpenseResponse> getExpensesByGroup(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new GroupNotFoundException(groupId);
        }

        List<Expense> expenses = expenseRepository.findByGroupId(groupId);
        return dtoMapper.toExpenseResponseList(expenses);
    }

    public List<BalanceResponse> calculateUserBalances(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        List<Balance> balances = balanceRepository.findByUserId(userId);
        return dtoMapper.toBalanceResponseList(balances);
    }

    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
        return dtoMapper.toExpenseResponse(expense);
    }

    // Keep existing split calculation methods...
    private List<ExpenseSplit> calculateSplits(Expense expense, ExpenseRequest request) {
        List<ExpenseSplit> splits = new ArrayList<>();

        switch (expense.getSplitType()) {
            case EQUAL:
                splits = calculateEqualSplit(expense, request.getParticipants());
                break;
            case PERCENTAGE:
                splits = calculatePercentageSplit(expense, request.getPercentages());
                break;
            case SHARE:
                splits = calculateShareSplit(expense, request.getShares());
                break;
            case EXACT:
                splits = calculateExactSplit(expense, request.getExactAmounts());
                break;
        }

        return splits;
    }

    private List<ExpenseSplit> calculateEqualSplit(Expense expense, List<Long> participants) {
        BigDecimal splitAmount = expense.getAmount().divide(
                new BigDecimal(participants.size()), 2, RoundingMode.HALF_UP);

        return participants.stream()
                .map(userId -> new ExpenseSplit(expense, userId, splitAmount))
                .collect(Collectors.toList());
    }

    private List<ExpenseSplit> calculatePercentageSplit(Expense expense, Map<Long, BigDecimal> percentages) {
        List<ExpenseSplit> splits = new ArrayList<>();

        for (Map.Entry<Long, BigDecimal> entry : percentages.entrySet()) {
            BigDecimal splitAmount = expense.getAmount()
                    .multiply(entry.getValue())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            ExpenseSplit split = new ExpenseSplit(expense, entry.getKey(), splitAmount);
            split.setSplitPercentage(entry.getValue());
            splits.add(split);
        }

        return splits;
    }

    private List<ExpenseSplit> calculateShareSplit(Expense expense, Map<Long, Integer> shares) {
        List<ExpenseSplit> splits = new ArrayList<>();
        int totalShares = shares.values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<Long, Integer> entry : shares.entrySet()) {
            BigDecimal splitAmount = expense.getAmount()
                    .multiply(new BigDecimal(entry.getValue()))
                    .divide(new BigDecimal(totalShares), 2, RoundingMode.HALF_UP);

            ExpenseSplit split = new ExpenseSplit(expense, entry.getKey(), splitAmount);
            split.setSplitShare(entry.getValue());
            splits.add(split);
        }

        return splits;
    }

    private List<ExpenseSplit> calculateExactSplit(Expense expense, Map<Long, BigDecimal> exactAmounts) {
        return exactAmounts.entrySet().stream()
                .map(entry -> new ExpenseSplit(expense, entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private void updateBalances(Expense expense) {
        Map<Long, BigDecimal> netBalances = new HashMap<>();

        netBalances.put(expense.getPaidBy(), expense.getAmount());

        for (ExpenseSplit split : expense.getSplits()) {
            netBalances.merge(split.getUserId(), split.getSplitAmount().negate(), BigDecimal::add);
        }

        for (Map.Entry<Long, BigDecimal> entry : netBalances.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) != 0) {
                Balance balance = new Balance(
                        entry.getKey(),
                        expense.getGroupId(),
                        expense.getPaidBy(),
                        entry.getValue()
                );
                balanceRepository.save(balance);
            }
        }
    }
}
