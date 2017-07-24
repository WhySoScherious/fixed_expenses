package com.earnest.util;

import java.math.BigDecimal;

public final class Money {
    public static final int PRECISION = 2;

    private static final String INVALID_AMOUNT_MSG = "Invalid money amount";

    public static BigDecimal parseMoney(String amount) throws NumberFormatException {
        if (amount == null) {
            throw new NumberFormatException(INVALID_AMOUNT_MSG);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount.length(); i++) {
            char curr = amount.charAt(i);
            if (curr == '$' || curr == ',') {
                continue;
            }

            sb.append(curr);
        }

        return new BigDecimal(sb.toString()).setScale(PRECISION);
    }

    public static String getMonetaryValue(BigDecimal amount) throws NullPointerException {
        if (amount == null) {
            throw new NullPointerException(INVALID_AMOUNT_MSG);
        }

        String amountStr = amount.toPlainString();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amountStr.length(); i++) {
            char curr = amountStr.charAt(i);
            if (Character.isDigit(curr)) {
                sb.append(curr);
            }
        }

        return sb.toString();
    }
}
