package dev.ed.shared.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum AccountType {
    SAVINGS,
    CHECKING,
    CREDIT_CARD,
    LOAN,
    INVESTMENT;

    public static AccountType getRandomAccountType() {
        AccountType[] values = AccountType.values();
        return values[ThreadLocalRandom.current().nextInt(values().length)];
    }
}
