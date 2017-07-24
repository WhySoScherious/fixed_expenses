package com.earnest.report;

import java.math.BigDecimal;
import java.util.Date;

import com.earnest.util.Money;

public class Tradeline {

    private static final int MORTGAGE_CODE = 10;
    private static final int MORTGAGE_SUBCODE_1 = 12;
    private static final int MORTGAGE_SUBCODE_2 = 15;
    private static final int EDUCATION_CODE = 5;

    private static final String TYPE_FIELD = "\"type\": \"";
    private static final String MONTHLY_PAYMENT_FIELD = "\"monthly_payment\": ";
    private static final String CURR_BALANCE_FIELD = "\"current_balance\": ";

    private final Date reportedDate;
    private final int code;
    private final int subcode;
    private final BigDecimal monthlyPayment;
    private final BigDecimal currentBalance;

    private enum TradelineType {
        mortgage, education, other
    }

    public Tradeline(TradelineBuilder builder) {
        this.reportedDate = builder.getReportedDate();
        this.code = builder.getCode();
        this.subcode = builder.getSubcode();
        this.monthlyPayment = builder.getMonthlyPayment();
        this.currentBalance = builder.getCurrentBalance();
    }

    public boolean hasZeroBalance() {
        return this.currentBalance.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isMortgageTradeline() {
        return this.code == MORTGAGE_CODE &&
                (this.subcode == MORTGAGE_SUBCODE_1 || this.subcode == MORTGAGE_SUBCODE_2);
    }

    public boolean isEducationTradeline() {
        return this.code == EDUCATION_CODE;
    }

    protected String printTypeJsonField() {
        return TYPE_FIELD + this.getType();
    }

    protected String printMonthlyPaymentJsonField() {
        return MONTHLY_PAYMENT_FIELD + Money.getMonetaryValue(this.monthlyPayment);
    }

    protected String printCurrentBalanceJsonField() {
        return CURR_BALANCE_FIELD + Money.getMonetaryValue(this.currentBalance);
    }

    private String getType() {
        String type = "";
        if (isMortgageTradeline()) {
            type = TradelineType.mortgage.toString();
        }
        else if (isEducationTradeline()) {
            type = TradelineType.education.toString();
        }
        else {
            type = TradelineType.other.toString();
        }

        return type;
    }

    public Date getReportedDate() {
        return reportedDate;
    }

    public int getCode() {
        return code;
    }

    public int getSubcode() {
        return subcode;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");
        sb.append('\t').append(printTypeJsonField()).append("\",\n");
        sb.append('\t').append(printMonthlyPaymentJsonField()).append(",\n");
        sb.append('\t').append(printCurrentBalanceJsonField()).append('\n');
        sb.append('}');

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tradeline other = (Tradeline) obj;
        if (code != other.code)
            return false;
        if (currentBalance == null) {
            if (other.currentBalance != null)
                return false;
        }
        else if (!currentBalance.equals(other.currentBalance))
            return false;
        if (monthlyPayment == null) {
            if (other.monthlyPayment != null)
                return false;
        }
        else if (!monthlyPayment.equals(other.monthlyPayment))
            return false;
        if (reportedDate == null) {
            if (other.reportedDate != null)
                return false;
        }
        else if (!reportedDate.equals(other.reportedDate))
            return false;
        if (subcode != other.subcode)
            return false;
        return true;
    }
}
