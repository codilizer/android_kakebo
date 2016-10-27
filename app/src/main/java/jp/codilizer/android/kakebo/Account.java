package jp.codilizer.android.kakebo;

import java.util.Date;

/**
 * Created by codilizer on 2016/04/31.
 */

// 収支
public class Account {
    int id;
    Common.AccountDivision accountDivision;
    String expenseDivision;
    String date;
    int amount;
    String memo;
    Date dtModified;

    Account(int aId,
            Common.AccountDivision aAccountDiv,
            String aExpenseDiv,
            String aDate,
            int aAmount,
            String aMemo,
            Date aDtModified) {
        id = aId;
        accountDivision = aAccountDiv;
        expenseDivision = aExpenseDiv;
        date = aDate;
        amount = aAmount;
        memo = aMemo;
        dtModified = aDtModified;
    }

}
