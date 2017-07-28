package com.earnest.report;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.earnest.util.Money;

public class CreditReport {
    private static BigDecimal NAT_AVG_MONTHLY_RENT = new BigDecimal(1061)
            .setScale(Money.PRECISION);

    private static final String FIXED_EXPENSES_FIELD = "\"fixed_expenses_before_education\": ";
    private static final String TRADELINES_FIELD = "\"tradelines\": [";

    private final List<Tradeline> tradelines;

    private boolean hasMortgageTradeline;

    private BigDecimal fixedExpensesBeforeEducation;

    public CreditReport() {
        this.tradelines = new LinkedList<Tradeline>();
        this.hasMortgageTradeline = false;
        this.fixedExpensesBeforeEducation = NAT_AVG_MONTHLY_RENT;
    }

    public void addTradelineAndUpdateFixedExpenses(Tradeline t) {
        if (t == null) {
            return;
        }

        this.tradelines.add(t);

        updateFixedExpensesBeforeEducation(t);
    }

    protected String printFixedExpensesBeforeEducationJsonField() {
        return FIXED_EXPENSES_FIELD + Money.getMonetaryValue(this.fixedExpensesBeforeEducation);
    }

    protected String printTradelinesJsonField() {
        StringBuilder sb = new StringBuilder();

        sb.append(TRADELINES_FIELD);
        for(int i = 0; i < this.tradelines.size(); i++) {
            if (i == 0) {
                sb.append('\n');
            }

            Tradeline curr = this.tradelines.get(i);
            sb.append(curr.toString());

            if (i != this.tradelines.size() - 1) {
                sb.append(',');
            }

            sb.append('\n');
        }
        sb.append(']');

        return sb.toString();
    }

    private void updateFixedExpensesBeforeEducation(Tradeline t) {
        if (t.hasZeroBalance()) {
            return;
        }

        if (t.isMortgageTradeline()) {
            handleMortgageTradeline(t);
        }
        else if (!t.isEducationTradeline()) {
            this.fixedExpensesBeforeEducation = this.fixedExpensesBeforeEducation.add(
                    t.getMonthlyPayment());
        }
    }

    private void handleMortgageTradeline(Tradeline t) {
        this.fixedExpensesBeforeEducation = this.fixedExpensesBeforeEducation.add(
                t.getMonthlyPayment());

        if (!this.hasMortgageTradeline) {
            this.fixedExpensesBeforeEducation = this.fixedExpensesBeforeEducation.subtract(
                    NAT_AVG_MONTHLY_RENT);

            this.hasMortgageTradeline = true;
        }
    }

    public BigDecimal getFixedExpensesBeforeEducation() {
        return fixedExpensesBeforeEducation;
    }

    public List<Tradeline> getTradelines() {
        return tradelines;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append(printFixedExpensesBeforeEducationJsonField()).append(",\n");
        sb.append(printTradelinesJsonField()).append('\n');
        sb.append('}');

        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fixedExpensesBeforeEducation == null)
                ? 0
                : fixedExpensesBeforeEducation.hashCode());
        result = prime * result + (hasMortgageTradeline ? 1231 : 1237);
        result = prime * result + ((tradelines == null) ? 0 : tradelines.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CreditReport other = (CreditReport) obj;
        if (fixedExpensesBeforeEducation == null) {
            if (other.fixedExpensesBeforeEducation != null)
                return false;
        }
        else if (!fixedExpensesBeforeEducation.equals(other.fixedExpensesBeforeEducation))
            return false;
        if (hasMortgageTradeline != other.hasMortgageTradeline)
            return false;
        if (tradelines == null) {
            if (other.tradelines != null)
                return false;
        }
        else if (!tradelines.equals(other.tradelines))
            return false;
        return true;
    }
}
