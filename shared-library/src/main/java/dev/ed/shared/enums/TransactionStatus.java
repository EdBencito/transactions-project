package dev.ed.shared.enums;

import java.util.concurrent.ThreadLocalRandom;


public enum TransactionStatus {
    PENDING,
    APPROVED,
    DECLINED,
    FLAGGED,
    REVERSED,
    CANCELLED;

    public static TransactionStatus getRandomStatus() {
        TransactionStatus[] values = {PENDING, APPROVED, DECLINED, REVERSED, CANCELLED};
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
