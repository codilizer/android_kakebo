package jp.codilizer.android.kakebo;

/**
 * Created by codilizer on 2016/04/28.
 */

public class CalDate {
    int year;
    int month;
    int day;

    public CalDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void set(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
