package com.earnest.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import com.earnest.utils.TradelineUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestTradeline {

    @Test
    public void testHasZeroBalance() {
        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.MORTGAGE_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, BigDecimal.ZERO, BigDecimal.ZERO));

        assertTrue(t.hasZeroBalance());
    }

    @Test
    public void testNotHasZeroBalance() {
        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.MORTGAGE_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, BigDecimal.ZERO, BigDecimal.ONE));

        assertFalse(t.hasZeroBalance());
    }

    @Test
    public void testIsMortgageTradeline() {
        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.MORTGAGE_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, BigDecimal.ZERO, BigDecimal.ONE));

        assertTrue(t.isMortgageTradeline());
    }

    @Test
    public void testNotIsMortgageTradeline() {
        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.EDUCATION_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, BigDecimal.ZERO, BigDecimal.ONE));

        assertFalse(t.isMortgageTradeline());
    }

    @Test
    public void testIsEducationTradeline() {
        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.EDUCATION_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, BigDecimal.ZERO, BigDecimal.ONE));

        assertTrue(t.isEducationTradeline());
    }

    @Test
    public void testNotIsEducationTradeline() {
        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.MORTGAGE_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, BigDecimal.ZERO, BigDecimal.ONE));

        assertFalse(t.isEducationTradeline());
    }

    @Test
    public void testTradelineJsonOutput() {
        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.MORTGAGE_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, BigDecimal.ZERO, BigDecimal.ONE));

        JsonObject tradeline = new JsonParser().parse(t.toString()).getAsJsonObject();
        assertEquals("mortgage", tradeline.get("type").getAsString());
        assertEquals("000", tradeline.get("monthly_payment").getAsString());
        assertEquals("100", tradeline.get("current_balance").getAsString());
    }
}
