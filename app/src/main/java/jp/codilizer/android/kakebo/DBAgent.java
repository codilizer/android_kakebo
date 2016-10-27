package jp.codilizer.android.kakebo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codilizer on 2016/04/30.
 */
public final class DBAgent {


    DBHelper mDBHelper;

    private static class DBAgentLoader {
        private static final DBAgent INSTANCE = new DBAgent();
    }

    private DBAgent() {
        if (DBAgentLoader.INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static DBAgent getInstance() {
        return DBAgentLoader.INSTANCE;
    }

    public void init(Context context) {
        mDBHelper = new DBHelper(context);
    }

    public void initDivisionList() {
        SQLiteDatabase db;

        db = mDBHelper.getWritableDatabase();
        db.execSQL("INSERT INTO income_division VALUES (null, '0', '小遣い')");
        db.execSQL("INSERT INTO income_division VALUES (null, '1', '仕事代')");
        db.execSQL("INSERT INTO income_division VALUES (null, '2', 'アルバイト代')");
        db.execSQL("INSERT INTO income_division VALUES (null, '3', '副収入')");
        db.execSQL("INSERT INTO income_division VALUES (null, '4', 'その他')");

        db.execSQL("INSERT INTO spending_division VALUES (null, '0', '食費')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '1', '文具代')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '2', '服代')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '3', '交通費')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '4', '雑費')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '5', '本代')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '6', '宿泊代')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '7', 'デート代')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '8', '光熱費')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '9', '定期代')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '10', '携帯代')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '11', '家賃')");
        db.execSQL("INSERT INTO spending_division VALUES (null, '12', 'その他')");
    }

    public ArrayList<String> getDivisionList(Common.AccountDivision accountDivision){
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor;

        ArrayList<String> divisionList = new ArrayList<String>();

        if(Common.AccountDivision.income == accountDivision) {
            cursor = db.rawQuery("select name from income_division", null);

            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                divisionList.add(name);
            }

        } else if (Common.AccountDivision.spending == accountDivision) {
            cursor = db.rawQuery("select name from spending_division", null);

            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                divisionList.add(name);
            }
        }

        return divisionList;
    }

    public void saveExepense(Common.AccountDivision aDiv, int aAmount,
                             String strDate, String aExpenseDivision, String aMemo) {
        ContentValues values = new ContentValues();

        values.put("account_division", String.valueOf(aDiv.getValue()).toString());
        values.put("expense_division", aExpenseDivision);
        values.put("accounting_date", strDate);
        values.put("amount", String.valueOf(aAmount).toString());
        values.put("memo", aMemo);


        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        boolean result = db.insert("account", null, values) > 0;
        db.close();
    }

    public ArrayList<Account> GetDailyExpense(String aDay) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor;

        ArrayList<Account> accountList = new ArrayList<Account>();

        String query = "select * from account where " +
                "accounting_date = date('" +
                aDay + "')";

        cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {
//            Common.AccountDivision accountDivision =
//                    Common.AccountDivision.getDivision(Integer.parseInt(cursor.getString(0)));

            int id = Integer.parseInt(cursor.getString(0));
            Common.AccountDivision accountDivision =
                    Common.AccountDivision.getDivision(Integer.parseInt(cursor.getString(1)));
            String expenseDivision = cursor.getString(2);
            String strDate = cursor.getString(3);
            int amount = Integer.parseInt(cursor.getString(4));
            String memo = cursor.getString(5);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dtModified;

            try {
                dtModified = df.parse(cursor.getString(6));

                Account account = new Account(
                        id,
                        accountDivision,
                        expenseDivision,
                        strDate,
                        amount,
                        memo,
                        dtModified);
                accountList.add(account);

            } catch(Exception e) {
                System.out.println("Error occurred"+ e.getMessage());
            }

        }
        return accountList;
    }

    public ArrayList<Account> getMonthlyExpense(String strStartDate, String strEndDate ) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor;

        ArrayList<Account> accountList = new ArrayList<Account>();

        String query = "select * from account where " +
                "accounting_date between date('" + strStartDate + "') and date('" +
                strEndDate + "')";

        cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {

            int id = Integer.parseInt(cursor.getString(0));
            Common.AccountDivision accountDivision =
                    Common.AccountDivision.getDivision(Integer.parseInt(cursor.getString(1)));
            String expenseDivision = cursor.getString(2);
            String strDate = cursor.getString(3);
            int amount = Integer.parseInt(cursor.getString(4));
            String memo = cursor.getString(5);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dtModified;

            try {
                dtModified = df.parse(cursor.getString(6));

                Account account = new Account(
                        id,
                        accountDivision,
                        expenseDivision,
                        strDate,
                        amount,
                        memo,
                        dtModified);
                accountList.add(account);

            } catch(Exception e) {
                System.out.println("Error occurred"+ e.getMessage());
            }

        }
        return accountList;
    }

    public Map<String, ArrayList<Account>> getExpensesByDay(List<String> dayList) {

        Map<String, ArrayList<Account>> mapAccountsByDay = new HashMap<>();

        int sizeDayList = dayList.size();

        ArrayList<Account> monthlyAccount = getMonthlyExpense(dayList.get(0), dayList.get(sizeDayList - 1));

        int sizeMonthlyAccount = monthlyAccount.size();


        for(int i=0; i < sizeDayList; i++) {
            String dateDay = dayList.get(i);

            ArrayList<Account> listAccountsDay = new ArrayList<Account>();

            for(int j=0; j < sizeMonthlyAccount; j++) {
                Account acc = monthlyAccount.get(j);

                if(acc.date.equals(dateDay)) {
                    listAccountsDay.add(acc);
                }
            }

            mapAccountsByDay.put(dateDay, listAccountsDay);

        }

        return mapAccountsByDay;
    }

    public ArrayList<AccountSummary> getAccountSummaryMonthly(List<String> aDayList) {
        Map<String, ArrayList<Account>> mapAccountsByDay = getExpensesByDay(aDayList);


        int sizeDayList = aDayList.size();

        ArrayList<AccountSummary> asList = new ArrayList<AccountSummary>();

        for(int i=0; i < sizeDayList; i++) {
            if(!mapAccountsByDay.get(aDayList.get(i)).isEmpty()) {
                ArrayList<Account> list = mapAccountsByDay.get(aDayList.get(i));
                int sizeList = list.size();

                int amountSpending=0;
                int amountIncome=0;

                for(int j=0; j< sizeList; j++) {

                    Account acc = list.get(j);
                    if(Common.AccountDivision.spending ==acc.accountDivision) {
                        amountSpending += acc.amount;
                    } else if (Common.AccountDivision.income==acc.accountDivision ) {
                        amountIncome += acc.amount;
                    }
                }

                AccountSummary as = new AccountSummary(amountIncome, amountSpending);
                asList.add(as);
            } else {
                AccountSummary as = new AccountSummary(0, 0);
                asList.add(as);
            }


        }

        return asList;
    }

    public AccountSummary GetAccountSummaryDay(String aDay) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor;

        ArrayList<Account> accountList = new ArrayList<Account>();

        String query = "select * from account where " +
                "accounting_date = date('" +
                aDay + "')";

        cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()) {

            int id = Integer.parseInt(cursor.getString(0));
            Common.AccountDivision accountDivision =
                    Common.AccountDivision.getDivision(Integer.parseInt(cursor.getString(1)));
            String expenseDivision = cursor.getString(2);
            String strDate = cursor.getString(3);
            int amount = Integer.parseInt(cursor.getString(4));
            String memo = cursor.getString(5);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dtModified;

            try {
                dtModified = df.parse(cursor.getString(6));

                Account account = new Account(
                        id,
                        accountDivision,
                        expenseDivision,
                        strDate,
                        amount,
                        memo,
                        dtModified);
                accountList.add(account);

            } catch(Exception e) {
                System.out.println("Error occurred"+ e.getMessage());
            }

        }

        int amountSpending=0;
        int amountIncome=0;

        int sizeList = accountList.size();

        for(int j=0; j< sizeList; j++) {

            Account acc = accountList.get(j);
            if(Common.AccountDivision.spending ==acc.accountDivision) {
                amountSpending += acc.amount;
            } else if (Common.AccountDivision.income==acc.accountDivision ) {
                amountIncome += acc.amount;
            }
        }

        AccountSummary as = new AccountSummary(amountIncome, amountSpending);

        return as;
    }
}
