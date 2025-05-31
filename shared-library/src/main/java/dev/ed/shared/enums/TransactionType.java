package dev.ed.shared.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum TransactionType {
    DEBIT,
    CREDIT,
    REVERSAl;

    public static TransactionType getRandomType() {
        TransactionType[] values = TransactionType.values();
        return values[ThreadLocalRandom.current().nextInt(values().length)];
    }
}
