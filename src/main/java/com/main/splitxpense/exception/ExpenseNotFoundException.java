package com.main.splitxpense.exception;

public class ExpenseNotFoundException extends SplitxpenseException {
    public ExpenseNotFoundException(Long expenseId) {
        super("Expense not found with ID: " + expenseId, "EXPENSE_NOT_FOUND");
    }
}
