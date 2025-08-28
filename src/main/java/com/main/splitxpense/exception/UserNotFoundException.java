package com.main.splitxpense.exception;

public class UserNotFoundException extends SplitxpenseException {
    public UserNotFoundException(Long userId) {
        super("User not found with ID: " + userId, "USER_NOT_FOUND");
    }

    public UserNotFoundException(String email) {
        super("User not found with email: " + email, "USER_NOT_FOUND");
    }
}
