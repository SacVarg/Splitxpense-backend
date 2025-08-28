package com.main.splitxpense.exception;

public class InsufficientPermissionException extends SplitxpenseException {
    public InsufficientPermissionException(String message) {
        super(message, "INSUFFICIENT_PERMISSION");
    }

    public static InsufficientPermissionException cannotModifyExpense(Long userId, Long expenseId) {
        return new InsufficientPermissionException("User " + userId + " cannot modify expense " + expenseId);
    }

    public static InsufficientPermissionException cannotAccessGroup(Long userId, Long groupId) {
        return new InsufficientPermissionException("User " + userId + " is not a member of group " + groupId);
    }
}
