package dev.ed.shared.enums;

import java.util.concurrent.ThreadLocalRandom;


public enum MerchantCategory {
    GAMBLING,
    ADULT_SERVICES,
    CRYPTOCURRENCY,
    FOOD_AND_DINING,
    ELECTRONICS_AND_TECHNOLOGY,
    HOME_AND_GARDEN,
    APPAREL_AND_ACCESSORIES,
    AUTOMOTIVE,
    ENTERTAINMENT,
    ONLINE_PURCHASES,
    HEALTH_AND_WELLNESS,
    TRAVEL,
    UTILITIES;

    public static MerchantCategory getRandomCategory() {
        MerchantCategory[] values = MerchantCategory.values();
        return values[ThreadLocalRandom.current().nextInt(values().length)];
    }

    public static MerchantCategory getRandomFraudulentCategory() {
        MerchantCategory[] values = {GAMBLING, ADULT_SERVICES, CRYPTOCURRENCY};
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

}
