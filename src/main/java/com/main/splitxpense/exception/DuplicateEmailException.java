package com.main.splitxpense.exception;

public class DuplicateEmailException extends SplitxpenseException {
    public DuplicateEmailException(String email) {
        super("Email already exists: " + email, "DUPLICATE_EMAIL");
    }
}
