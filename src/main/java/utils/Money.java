package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Money {
    private Money() {}

    public static BigDecimal parse(String text) {
        return new BigDecimal(text.replaceAll("[^0-9.]", ""));
    }

    public static String plain(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    public static String format(BigDecimal amount) {
        return "$" + plain(amount);
    }
}