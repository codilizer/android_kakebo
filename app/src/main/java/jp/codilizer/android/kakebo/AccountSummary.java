package jp.codilizer.android.kakebo;

/**
 * Created by codilizer on 2016/04/01.
 */
public class AccountSummary {
    int totalIncome;
    int totalSpending;
    int budget;
    String DayMemo = null;

    AccountSummary(int aTotalIncome, int aTotalSpending) {
        totalIncome = aTotalIncome;
        totalSpending = aTotalSpending;
    }

    AccountSummary(int aTotalIncome, int aTotalSpending, int aBudget) {
        totalIncome = aTotalIncome;
        totalSpending = aTotalSpending;
        budget = aBudget;
    }

    AccountSummary(int aTotalIncome, int aTotalSpending, String aDayMemo) {
        totalIncome = aTotalIncome;
        totalSpending = aTotalSpending;
        DayMemo = aDayMemo;
    }

    AccountSummary(int aTotalIncome, int aTotalSpending, int aBudget, String aDayMemo) {
        totalIncome = aTotalIncome;
        totalSpending = aTotalSpending;
        budget = aBudget;
        DayMemo = aDayMemo;
    }
}
