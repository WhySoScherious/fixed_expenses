package com.earnest.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

public class TestMoney {
    @Test
    public void testParseMoney() {
        String amount = "2113.12";

        BigDecimal bd = Money.parseMoney(amount);

        assertEquals(new BigDecimal(amount).setScale(Money.PRECISION), bd);
    }

    @Test
    public void testParseMoneyWithDollarSign() {
        String amount = "$2113.12";

        BigDecimal bd = Money.parseMoney(amount);

        assertEquals(new BigDecimal("2113.12").setScale(Money.PRECISION), bd);
    }

    @Test
    public void testParseMoneyWithComma() {
        String amount = "$2,113.12";

        BigDecimal bd = Money.parseMoney(amount);

        assertEquals(new BigDecimal("2113.12").setScale(Money.PRECISION), bd);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseMoneyEmpty() {
        String amount = "";

        Money.parseMoney(amount);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseMoneyInvalid() {
        String amount = "dollars12.12";

        Money.parseMoney(amount);
    }

    @Test(expected = NumberFormatException.class)
    public void testParseMoneyNull() {
        String amount = null;

        Money.parseMoney(amount);
    }

    @Test
    public void testGetMonetaryValue() {
        BigDecimal bdAmount = new BigDecimal("2113.12").setScale(Money.PRECISION);

        String amount = Money.getMonetaryValue(bdAmount);

        assertEquals("211312", amount);
    }

    @Test
    public void testGetMonetaryValueZeroCents() {
        BigDecimal bdAmount = new BigDecimal("2113.00").setScale(Money.PRECISION);

        String amount = Money.getMonetaryValue(bdAmount);

        assertEquals("211300", amount);
    }

    @Test
    public void testGetMonetaryValueNoScale() {
        BigDecimal bdAmount = new BigDecimal("2113").setScale(Money.PRECISION);

        String amount = Money.getMonetaryValue(bdAmount);

        assertEquals("211300", amount);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMonetaryValueNull() {
        BigDecimal bdAmount = null;

        String amount = Money.getMonetaryValue(bdAmount);

        assertNull(amount);
    }
}
