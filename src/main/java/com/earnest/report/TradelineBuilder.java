package com.earnest.report;

import java.math.BigDecimal;
import java.util.Date;

import com.earnest.util.Money;

public class TradelineBuilder implements Builder<Tradeline> {

    private final Date reportedDate;

    private final int code;

    private final int subcode;

    private final BigDecimal monthlyPayment;

    private final BigDecimal currentBalance;

    public TradelineBuilder(Date reportedDate, int code, int subcode, BigDecimal monthlyPayment,
            BigDecimal currentBalance) {
        this.reportedDate = reportedDate;
        this.code = code;
        this.subcode = subcode;
        this.monthlyPayment = monthlyPayment.setScale(Money.PRECISION);
        this.currentBalance = currentBalance.setScale(Money.PRECISION);
    };

    public Date getReportedDate() {
        return this.reportedDate;
    }

    public int getCode() {
        return this.code;
    }

    public int getSubcode() {
        return this.subcode;
    }

    public BigDecimal getMonthlyPayment() {
        return this.monthlyPayment;
    }

    public BigDecimal getCurrentBalance() {
        return this.currentBalance;
    }

    @Override
    public Tradeline build() {
        return new Tradeline(this);
    }
}
