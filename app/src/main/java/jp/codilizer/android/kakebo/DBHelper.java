package jp.codilizer.android.kakebo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by codilizer on 2016/04/30.
 */
public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context) {
        super(context, "AccountBook.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table account( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "account_division int, " +
                "expense_division text, " +
                "accounting_date date, " +
                "amount int, " +
                "memo text, " + //実際は要らないカーラム
                "modified_datetime datetime default current_timestamp, " +
                "deleted BOOL NOT NULL DEFAULT 0" +
                ");");

        db.execSQL("create table daily_memo( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "day date, " +
                "memo text" +
                ");");

        db.execSQL("create table account_division( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "code int, " +
                "name text" +
                ");");

        db.execSQL("create table income_division( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "code int, " +
                "name text"+
                ");");

        db.execSQL("create table spending_division( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "code int, " +
                "name text" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
