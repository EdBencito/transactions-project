package dev.ed.shared.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum TransactionType {
    DEBIT,
    CREDIT,
    REVERSAL;

    public static TransactionType getRandomType() {
        TransactionType[] values = {DEBIT, CREDIT};
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
