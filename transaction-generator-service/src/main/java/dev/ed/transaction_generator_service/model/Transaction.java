package dev.ed.transaction_generator_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private UUID transactionId;

    @NotBlank(message = "accountId is required")
    private String accountId;

    private LocalDateTime timestamp;

    @NotNull(message = "transactionType is required")
    private TransactionType transactionType;

    @NotNull(message = "amount is required")
    private BigDecimal amount;

    @NotBlank(message = "currency is required")
    private String currency;

    @NotBlank(message = "merchantCategory is required")
    private MerchantCategory merchantCategory;

    private MerchantLocation merchantLocation;

    private DeviceInfo deviceInfo;

    private String ipAddress;

    @NotNull(message = "transactionChannel is required")
    private TransactionChannel transactionChannel;

    private boolean isFraudulent;

    // Enums and Inner Classes
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MerchantLocation {
        private String city;
        private String country;

//        @Override
//        public String toString() {
//            return "MerchantLocation{" +
//                    "city='" + city + '\'' +
//                    ", country='" + country + '\'' +
//                    '}';
//        }
    }

    public enum DeviceInfo {
        ROOTED_DEVICE,
        EMULATOR,
        WEB_BROWSER,
        MOBILE_APP,
        ATM,
        POS_TERMINAL;

        public static DeviceInfo getRandomInfo() {
            DeviceInfo[] values = DeviceInfo.values();
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }

        public static DeviceInfo getRandomFraudulentInfo() {
            DeviceInfo[] values = {MOBILE_APP,EMULATOR,WEB_BROWSER,ROOTED_DEVICE,};
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }
    }

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
            MerchantCategory[] values = {GAMBLING,ADULT_SERVICES,CRYPTOCURRENCY};
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }
    }

    public enum TransactionType {
        DEBIT,
        CREDIT,
        TRANSFER,
        PURCHASE,
        WITHDRAWAL,
        DEPOSIT,
        PAYMENT;

        public static TransactionType getRandomType() {
            TransactionType[] values = TransactionType.values();
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }
    }

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
            TransactionChannel[] values = {MOBILE_APP,WEB};
            return values[ThreadLocalRandom.current().nextInt(values().length)];
        }
    }

//    @Override
//    public String toString() {
//        return "TransactionEvent{" +
//                "transactionId=" + transactionId +
//                ", accountId='" + accountId + '\'' +
//                ", timestamp=" + timestamp +
//                ", transactionType=" + transactionType +
//                ", amount=" + amount +
//                ", currency='" + currency + '\'' +
//                ", merchantCategory='" + merchantCategory + '\'' +
//                ", merchantLocation=" + merchantLocation +
//                ", deviceInfo=" + deviceInfo +
//                ", ipAddress='" + ipAddress + '\'' +
//                ", transactionChannel=" + transactionChannel +
//                ", isFraudulent=" + isFraudulent +
//                '}';
//    }
}