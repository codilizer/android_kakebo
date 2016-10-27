package jp.codilizer.android.kakebo;

/**
 * Created by codilizer on 2016/04/29.
 */

//詳細画面リスト用
public class ExpenseItem
{
    String mExpenseName;
    String mIncome;
    String mExpending;

    ExpenseItem(String aExpenseName, String aIncome, String aExpending)
    {
        mExpenseName = aExpenseName;
        mIncome = aIncome;
        mExpending = aExpending;
    }
}
