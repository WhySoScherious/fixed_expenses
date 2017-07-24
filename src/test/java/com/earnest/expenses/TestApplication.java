package com.earnest.expenses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

import com.earnest.report.Tradeline;
import com.earnest.util.Money;

public class TestApplication {
    private static final int LINE_NUMBER = 1;

    @Test
    public void testParseTradeline() {
        String lineInput = "2015-10-10 10 12 $1470.31 $659218.00";

        Tradeline t = Application.parseTradeline(lineInput, LINE_NUMBER);

        assertEquals(10, t.getCode());
        assertEquals(12, t.getSubcode());
        assertEquals(new BigDecimal("1470.31").setScale(Money.PRECISION), t.getMonthlyPayment());
        assertEquals(new BigDecimal("659218.00").setScale(Money.PRECISION), t.getCurrentBalance());
    }

    @Test
    public void testParseTradelineNull() {
        String lineInput = null;

        Tradeline t = Application.parseTradeline(lineInput, LINE_NUMBER);

        assertNull(t);
    }

    @Test
    public void testParseTradelineEmpty() {
        String lineInput = "";

        Tradeline t = Application.parseTradeline(lineInput, LINE_NUMBER);

        assertNull(t);
    }

    @Test
    public void testParseTradelineInvalidDate() {
        String lineInput = "2015-1010 10 12 $1470.31 $659218.00";

        Tradeline t = Application.parseTradeline(lineInput, LINE_NUMBER);

        assertNull(t);
    }

    @Test
    public void testParseTradelineInvalidCode() {
        String lineInput = "2015-10-10 a 12 $1470.31 $659218.00";

        Tradeline t = Application.parseTradeline(lineInput, LINE_NUMBER);

        assertNull(t);
    }

    @Test
    public void testParseTradelineInvalidSubcode() {
        String lineInput = "2015-10-10 10 b $1470.31 $659218.00";

        Tradeline t = Application.parseTradeline(lineInput, LINE_NUMBER);

        assertNull(t);
    }

    @Test
    public void testParseTradelineInvalidMonthlyPayment() {
        String lineInput = "2015-10-10 10 12 $14a70.31 $659218.00";

        Tradeline t = Application.parseTradeline(lineInput, LINE_NUMBER);

        assertNull(t);
    }

    @Test
    public void testParseTradelineInvalidBalance() {
        String lineInput = "2015-10-10 10 12 $1470.31 $659218.v00";

        Tradeline t = Application.parseTradeline(lineInput, LINE_NUMBER);

        assertNull(t);
    }
}
