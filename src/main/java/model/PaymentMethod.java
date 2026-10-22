package model;

public enum PaymentMethod {
    BANK_TRANSFER("Bank Transfer"),
    CASH_ON_DELIVERY("Cash on Delivery"),
    CREDIT_CARD("Credit Card"),
    GIFT_CARD("Gift Card");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
