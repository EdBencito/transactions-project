package dev.ed.shared.enums;

import java.util.concurrent.ThreadLocalRandom;

public enum TransactionChannel {
    WEB,
    MOBILE_APP,
    ATM,
    POS,
    PHONE_BANKING,
    BRANCH;

    public static TransactionChannel getRandomChannel() {
        TransactionChannel[] values = TransactionChannel.values();
        return values[ThreadLocalRandom.current().nextInt(values().length)];
    }

    public static TransactionChannel getRandomFraudulentChannel() {
        TransactionChannel[] values = {MOBILE_APP, WEB};
        return values[ThreadLocalRandom.current().nextInt(values().length)];
    }
}