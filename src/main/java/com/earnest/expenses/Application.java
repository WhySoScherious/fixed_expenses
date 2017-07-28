package com.earnest.expenses;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.earnest.report.CreditReport;
import com.earnest.report.Tradeline;
import com.earnest.report.TradelineBuilder;
import com.earnest.util.Money;

public class Application {

    private static final int VALID_ARG_COUNT = 2;
    private static final int OPTION_INDEX = 0;
    private static final int REPORT_PATH_INDEX = 1;

    private static final int REPORT_VALUES_LENGTH = 5;

    private static final int DATE_INDEX = 0;
    private static final int CODE_INDEX = 1;
    private static final int SUBCODE_INDEX = 2;
    private static final int MONTHLY_PAYMENT_INDEX = 3;
    private static final int CURR_BALANCE_INDEX = 4;

    private static final DateFormat REPORTED_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final String REPORT_PATH_OPTION = "-f";
    private static final String USAGE = "Usage: java -jar <jar name> [-f report path]";
    private static final String INVALID_OPTION = ": Invalid option";
    private static final String INVALID_INPUT_MSG = ": Invalid line input: ";
    private static final String INVALID_DATA_AMT_MSG = ": Missing data field(s)";

    public static void main(String[] args) {
        String reportPath = null;
        if (args.length != 0) {
            reportPath = parseOptions(args);
        }

        try {
            printCreditReportJson(reportPath);
        }
        catch (FileNotFoundException e) {
            System.err.println(reportPath + ": File not found");

            System.exit(ExitStatus.FILE_NOT_FOUND.code());
        }
        catch (IOException e) {
            System.err.println(reportPath + ": I/O error occurred");
            e.printStackTrace();

            System.exit(ExitStatus.IO_ERROR_OCCURRED.code());
        }

        System.exit(ExitStatus.SUCCESS.code());
    }

    protected static Tradeline parseTradeline(String line, int lineNumber) {
        Tradeline t = null;
        if (line == null) {
            return t;
        }

        String[] values = line.split("[ ]+");
        if (values.length != REPORT_VALUES_LENGTH) {
            System.err.println(lineNumber + INVALID_DATA_AMT_MSG);
            return t;
        }

        try {
            Date reportedDate = REPORTED_DATE_FORMAT.parse(values[DATE_INDEX]);
            int code = Integer.parseInt(values[CODE_INDEX]);
            int subcode = Integer.parseInt(values[SUBCODE_INDEX]);
            BigDecimal monthlyPayment = Money.parseMoney(values[MONTHLY_PAYMENT_INDEX]);
            BigDecimal currentBalance = Money.parseMoney(values[CURR_BALANCE_INDEX]);

            t = new TradelineBuilder(reportedDate, code, subcode, monthlyPayment, currentBalance)
                    .build();
        }
        catch(NumberFormatException | ParseException e) {
            System.err.println(lineNumber + INVALID_INPUT_MSG + e.getMessage());
        }

        return t;
    }

    private static String parseOptions(String[] args) {
        String reportPath = null;
        if (args.length > VALID_ARG_COUNT) {
            System.err.println(USAGE);
            System.exit(ExitStatus.INVALID_ARG_COUNT.code());
        }

        String option = args[OPTION_INDEX]; 
        switch(option) {
            case REPORT_PATH_OPTION:
                reportPath = args[REPORT_PATH_INDEX];
                break;
            default:
                System.err.println(option + INVALID_OPTION);
                System.err.println(USAGE);
                System.exit(ExitStatus.INVALID_OPTION.code());
        }

        return reportPath;
    }

    private static void printCreditReportJson(String reportPath) throws IOException {
        BufferedReader br = null;
        Reader reader = null;

        try {
            if (reportPath != null) {
                reader = new FileReader(reportPath);
            }
            else {
                reader = new InputStreamReader(System.in);
            }

            br = new BufferedReader(reader);
            CreditReport cr = populateCreditReport(br);

            // Print parsed credit report with derived facts
            System.out.println(cr.toString());
        }
        finally {
            if (br != null) {
                br.close();
            }

            if (reader != null) {
                reader.close();
            }
        }
    }

    private static CreditReport populateCreditReport(BufferedReader br) throws IOException {
        CreditReport cr = new CreditReport();

        int lineNumber = 1;
        String line = br.readLine();
        while (line != null) {
            Tradeline currTradeline = parseTradeline(line, lineNumber);

            cr.addTradelineAndUpdateFixedExpenses(currTradeline);

            lineNumber++;
            line = br.readLine();
        }

        br.close();

        return cr;
    }
}
