package com.main.splitxpense.exception;

import java.math.BigDecimal;

public class InvalidSplitException extends SplitxpenseException {
    public InvalidSplitException(String message) {
        super(message, "INVALID_SPLIT");
    }

    public static InvalidSplitException percentageNotAddUpTo100(BigDecimal total) {
        return new InvalidSplitException("Percentage split must add up to 100%. Current total: " + total + "%");
    }

    public static InvalidSplitException exactAmountMismatch(BigDecimal expenseAmount, BigDecimal splitTotal) {
        return new InvalidSplitException("Exact split amounts (" + splitTotal + ") don't match expense amount (" + expenseAmount + ")");
    }

    public static InvalidSplitException emptyParticipants() {
        return new InvalidSplitException("No participants provided for split");
    }

    public static InvalidSplitException invalidShareValue() {
        return new InvalidSplitException("Share values must be positive integers");
    }
}
