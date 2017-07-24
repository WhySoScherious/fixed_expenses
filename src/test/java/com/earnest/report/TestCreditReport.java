package com.earnest.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import com.earnest.util.Money;
import com.earnest.utils.CreditReportUtil;
import com.earnest.utils.TradelineUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestCreditReport {
    private static final BigDecimal HUNDRED = new BigDecimal("100").setScale(Money.PRECISION);

    @Test
    public void testAddTradelineAndUpdateFixedExpenses() {
        CreditReport cr = new CreditReport();

        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.MORTGAGE_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, BigDecimal.ONE, HUNDRED));

        cr.addTradelineAndUpdateFixedExpenses(t);

        assertTrue(cr.getTradelines().size() == 1);
        assertEquals(BigDecimal.ONE.setScale(Money.PRECISION),
                cr.getFixedExpensesBeforeEducation());
    }

    @Test
    public void testAddTradelineAndUpdateFixedExpensesZeroBalance() {
        CreditReport cr = new CreditReport();

        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.MORTGAGE_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, BigDecimal.ONE, BigDecimal.ZERO));

        cr.addTradelineAndUpdateFixedExpenses(t);

        assertTrue(cr.getTradelines().size() == 1);
        assertEquals(CreditReportUtil.NAT_AVG_MONTHLY_RENT, cr.getFixedExpensesBeforeEducation());
    }

    @Test
    public void testAddTradelineAndUpdateFixedExpensesOnlyEducationExpense() {
        CreditReport cr = new CreditReport();

        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.EDUCATION_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, BigDecimal.ONE, HUNDRED));

        cr.addTradelineAndUpdateFixedExpenses(t);

        assertTrue(cr.getTradelines().size() == 1);
        assertEquals(CreditReportUtil.NAT_AVG_MONTHLY_RENT, cr.getFixedExpensesBeforeEducation());
    }

    @Test
    public void testAddTradelineAndUpdateFixedExpensesOnlyOtherExpense() {
        CreditReport cr = new CreditReport();

        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.OTHER_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, HUNDRED, HUNDRED));

        cr.addTradelineAndUpdateFixedExpenses(t);

        assertTrue(cr.getTradelines().size() == 1);
        assertEquals(CreditReportUtil.NAT_AVG_MONTHLY_RENT.add(HUNDRED),
                cr.getFixedExpensesBeforeEducation());
    }

    @Test
    public void testCreditReportJsonOutput() {
        Tradeline t = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.MORTGAGE_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, HUNDRED, BigDecimal.ONE));

        CreditReport cr = new CreditReport();
        cr.addTradelineAndUpdateFixedExpenses(t);

        JsonObject report = new JsonParser().parse(cr.toString()).getAsJsonObject();

        String expectedFixedExpenses = Money.getMonetaryValue(HUNDRED);
        assertEquals(expectedFixedExpenses, report.get("fixed_expenses_before_education").getAsString());

        JsonObject tradeline = report.getAsJsonArray("tradelines").get(0).getAsJsonObject();
        assertEquals("mortgage", tradeline.get("type").getAsString());
        assertEquals("10000", tradeline.get("monthly_payment").getAsString());
        assertEquals("100", tradeline.get("current_balance").getAsString());
    }

    @Test
    public void testCreditReportJsonOutputMultipleTradelines() {
        Tradeline t1 = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.MORTGAGE_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, HUNDRED, BigDecimal.ONE));

        Tradeline t2 = new Tradeline(
                new TradelineBuilder(
                        TradelineUtil.TEST_DATE, TradelineUtil.EDUCATION_CODE,
                        TradelineUtil.MORTGAGE_SUBCODE_1, HUNDRED, BigDecimal.ONE));

        CreditReport cr = new CreditReport();
        cr.addTradelineAndUpdateFixedExpenses(t1);
        cr.addTradelineAndUpdateFixedExpenses(t2);

        JsonObject report = new JsonParser().parse(cr.toString()).getAsJsonObject();

        String expectedFixedExpenses = Money.getMonetaryValue(HUNDRED);
        assertEquals(expectedFixedExpenses, report.get("fixed_expenses_before_education").getAsString());

        JsonArray tradelines = report.getAsJsonArray("tradelines");
                
        JsonObject tradeline1 = tradelines.get(0).getAsJsonObject();
        assertEquals("mortgage", tradeline1.get("type").getAsString());
        assertEquals("10000", tradeline1.get("monthly_payment").getAsString());
        assertEquals("100", tradeline1.get("current_balance").getAsString());

        JsonObject tradeline2 = tradelines.get(1).getAsJsonObject();
        assertEquals("education", tradeline2.get("type").getAsString());
        assertEquals("10000", tradeline2.get("monthly_payment").getAsString());
        assertEquals("100", tradeline2.get("current_balance").getAsString());
    }

    @Test
    public void testCreditReportJsonOutputNoTradelines() {
        CreditReport cr = new CreditReport();

        JsonObject report = new JsonParser().parse(cr.toString()).getAsJsonObject();

        String expectedFixedExpenses = Money.getMonetaryValue(CreditReportUtil.NAT_AVG_MONTHLY_RENT);

        assertEquals(expectedFixedExpenses, report.get("fixed_expenses_before_education").getAsString());
        assertEquals("[]", report.get("tradelines").getAsJsonArray().toString());
    }
}
