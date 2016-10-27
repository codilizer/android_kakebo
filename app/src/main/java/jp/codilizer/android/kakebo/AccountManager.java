package jp.codilizer.android.kakebo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by codilizer on 2016/04/01.
 */


public final class AccountManager {

    private ArrayList<AccountSummary> mArrayAccountSummary;

    private  Map<String, ArrayList<Account>> mMapAccountList;

    AccountSummary mAccountSummarySelectedDay;

    public CalDate mCurrentDate;
    int mTotalMonthlyIncome = 0;
    int mTotalMonthleSpending = 0;
    int mTotalMonthBalance = 0;

    private static class AccountManagerLoader {
        private static final AccountManager INSTANCE = new AccountManager();
    }

    private AccountManager() {
        if (AccountManagerLoader.INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static AccountManager getInstance() {
        return AccountManagerLoader.INSTANCE;
    }

    //　注意：最初 CalendarAdapter から呼ばれる。
    public void refreshMonthlyAccount(List<String> aDayList) {
        mArrayAccountSummary = DBAgent.getInstance().getAccountSummaryMonthly(aDayList);
        mCurrentDate = DateManager.getInstance().getCurDate();

        calcMonthTotal(aDayList);
    }

    private void calcMonthTotal(List<String> aDayList) {

        int size = mArrayAccountSummary.size();


        mTotalMonthlyIncome = 0;
        mTotalMonthleSpending = 0;

        for(int i=0; i < size; i++) {

            AccountSummary acc = mArrayAccountSummary.get(i);
            String[] separatedTime = aDayList.get(i).split("-");

            if(Integer.parseInt(separatedTime[1]) == mCurrentDate.month) {
                mTotalMonthlyIncome += acc.totalIncome;
                mTotalMonthleSpending += acc.totalSpending;
            }
        }
        mTotalMonthBalance = mTotalMonthlyIncome - mTotalMonthleSpending;
    }

    public int GetMonthlyIncome(){
        return mTotalMonthlyIncome;
    }

    public int GetMonthlySpending() {
        return mTotalMonthleSpending;
    }

    public int GetMonthBalance() {
        return mTotalMonthBalance;
    }

    public void SetCurrentDay(int pos) {

        mAccountSummarySelectedDay = mArrayAccountSummary.get(pos);
    }

    public void SetCurrentDay(String aDay) {
        mAccountSummarySelectedDay = DBAgent.getInstance().GetAccountSummaryDay(aDay);
    }

    public AccountSummary GetDailyAccountSummary() {
        return mAccountSummarySelectedDay;
    }


    public ArrayList<AccountSummary> getAccountSummaryMonthly() {

        return mArrayAccountSummary;
    }

    public ArrayList<Account> GetExpenseListByDay (String aStrDate) {
        return DBAgent.getInstance().GetDailyExpense(aStrDate);
    }


}

