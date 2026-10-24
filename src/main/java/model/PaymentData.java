package model;

import java.util.Map;

public record PaymentData(PaymentMethod method, Map<String, String> fields) {
    public static final String CARD_NUMBER = "credit_card_number";
    public static final String CARD_EXPIRATION = "expiration_date";
    public static final String CARD_CVV = "cvv";
    public static final String CARD_HOLDER = "card_holder_name";
    public static final String BANK_NAME = "bank_name";
    public static final String ACCOUNT_NAME = "account_name";
    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String GIFT_CARD_NUMBER = "gift_card_number";
    public static final String VALIDATION_CODE = "validation_code";

    public static PaymentData creditCard() {
        return new PaymentData(PaymentMethod.CREDIT_CARD, Map.of(
                CARD_NUMBER, "1111-1111-1111-1111",
                CARD_EXPIRATION, "12/2030",
                CARD_CVV, "123",
                CARD_HOLDER, "John Doe"
        ));
    }

    public static PaymentData bankTransfer() {
        return new PaymentData(PaymentMethod.BANK_TRANSFER, Map.of(
                BANK_NAME, "Test Bank",
                ACCOUNT_NAME, "John Doe",
                ACCOUNT_NUMBER, "1234567890"
        ));
    }

    public static PaymentData cashOnDelivery() {
        return new PaymentData(PaymentMethod.CASH_ON_DELIVERY, Map.of());
    }

    public static PaymentData giftCard() {
        return new PaymentData(PaymentMethod.GIFT_CARD, Map.of(
                GIFT_CARD_NUMBER, "1234567890987654",
                VALIDATION_CODE, "1234"
        ));
    }

    @Override
    public String toString() {
        return "PaymentData[method=" + method.label() + "]";
    }
}