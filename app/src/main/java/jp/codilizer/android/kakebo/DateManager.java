package jp.codilizer.android.kakebo;

import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by codilizer on 2016/04/28.
 */

public final class DateManager {

    private GregorianCalendar gCalendar;
    private CalDate curDate;

    private static class DateManagerLoader {
        private static final DateManager INSTANCE = new DateManager();
    }

    private DateManager() {
        if (DateManagerLoader.INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static DateManager getInstance() {
        return DateManagerLoader.INSTANCE;
    }


    public void initDateManager() {
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");

        gCalendar = (GregorianCalendar) GregorianCalendar.getInstance(tz);

        print(gCalendar);

        initCurDate(gCalendar);
    }

    private void initCurDate(GregorianCalendar cal) {
        curDate = new CalDate(cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH) + 1,
                            cal.get(Calendar.DAY_OF_MONTH));
    }

    private void setCurDate(GregorianCalendar cal) {
        curDate.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));

        Log.d("[$$$$$$$$$$] ", "curDate : " + curDate.year + ", " +
                                                curDate.month + ", " +
                                                curDate.day);
    }

    public void setCurDate(String dateString) {
        String[] separatedTime = dateString.split("-");
        gCalendar.set(Integer.parseInt(separatedTime[0]),
                        Integer.parseInt(separatedTime[1])-1,
                        Integer.parseInt(separatedTime[2]));

        setCurDate(gCalendar);

        Log.d("[$$$$$$$$$$] ", "curDate : " + curDate.year + ", " +
                curDate.month + ", " +
                curDate.day);
    }

    public void setCurDate(int year, int month, int day)
    {
        gCalendar.set(year, month, day);
        setCurDate(gCalendar);

        Log.d("[$$$$$$$$$$] ", "curDate : " + curDate.year + ", " +
                curDate.month + ", " +
                curDate.day);
    }


    public CalDate getCurDate() {
        return curDate;
    }

    public String getCurDateString() {
        String curDateString =  String.valueOf(curDate.year) + "-" +
                String.format("%1$02d", curDate.month) + "-" +
                String.format("%1$02d", curDate.day);

        Log.d("[DEBUG] ", "curDateString : " + curDateString );
        return curDateString;
    }

    public GregorianCalendar getCurCalendar() {
        return gCalendar;
    }

    public GregorianCalendar getCurCalendarClone() {
        CalDate curDate = getCurDate();

        return new GregorianCalendar(curDate.year, curDate.month, 1);
    }

    public GregorianCalendar cloneCalendar(GregorianCalendar cal) {

        return new GregorianCalendar(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
    }

    public void moveNextMonth() {
        Log.d("[DEBUG] ", "DateManager::moveNextMonth() called! ");

        // 移動する月の最大日が現在日より小さい場合問題があるので1日で設定する。
        gCalendar.set(Calendar.DAY_OF_MONTH, 1);

        if (gCalendar.get(Calendar.MONTH) == gCalendar.getActualMaximum(Calendar.MONTH)) {

            gCalendar.set(Calendar.YEAR, gCalendar.get(Calendar.YEAR) + 1);
            gCalendar.set(Calendar.MONTH, gCalendar.getActualMinimum(Calendar.MONTH));
            //gCalendar.set(Calendar.DAY_OF_MONTH, gCalendar.get(Calendar.DAY_OF_MONTH));

        } else {
            gCalendar.set(Calendar.YEAR, gCalendar.get(Calendar.YEAR));
            gCalendar.set(Calendar.MONTH, gCalendar.get(Calendar.MONTH)+1);
            //gCalendar.set(Calendar.DAY_OF_MONTH, gCalendar.get(Calendar.DAY_OF_MONTH));
        }

        int orgDay = curDate.day;
        int maxDay = gCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if( orgDay > maxDay) {
            gCalendar.set(Calendar.DAY_OF_MONTH, maxDay);
        } else {
            gCalendar.set(Calendar.DAY_OF_MONTH, orgDay);
        }

        setCurDate(gCalendar);
    }

    public void movePreviousMonth() {

        Log.d("[DEBUG] ", "DateManager::movePreviousMonth() called! ");

        // 移動する月の最大日が現在日より小さい場合問題があるので1日で設定する。
        gCalendar.set(Calendar.DAY_OF_MONTH, 1);

        if (gCalendar.get(Calendar.MONTH) == gCalendar.getActualMinimum(Calendar.MONTH)) {

            gCalendar.set(Calendar.YEAR, gCalendar.get(Calendar.YEAR)-1);
            gCalendar.set(Calendar.MONTH, gCalendar.getActualMaximum(Calendar.MONTH));
            gCalendar.set(Calendar.DAY_OF_MONTH, gCalendar.get(Calendar.DAY_OF_MONTH));
        } else {

            gCalendar.set(Calendar.YEAR, gCalendar.get(Calendar.YEAR));
            gCalendar.set(Calendar.MONTH, gCalendar.get(Calendar.MONTH)-1);
            gCalendar.set(Calendar.DAY_OF_MONTH, gCalendar.get(Calendar.DAY_OF_MONTH));
        }

        int orgDay = curDate.day;
        int maxDay = gCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if( orgDay > maxDay) {
            gCalendar.set(Calendar.DAY_OF_MONTH, maxDay);
        } else {
            gCalendar.set(Calendar.DAY_OF_MONTH, orgDay);
        }

        setCurDate(gCalendar);
    }

    public void moveNextDay() {
        gCalendar.add(Calendar.DAY_OF_MONTH, +1);

        setCurDate(gCalendar);
    }

    public void movePreviousDay() {
        gCalendar.add(Calendar.DAY_OF_MONTH, -1);

        setCurDate(gCalendar);
    }

    public void print(GregorianCalendar cal) {

        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        Log.d("[DEBUG] ", "DateManager::print() called! ");
        Log.d("[DEBUG] ", "Year : " + year + " Month : " + month + " DAY : " + dayOfMonth);

    }

    public int getFirstDay() {

        CalDate date = getCurDate();

        GregorianCalendar tempCal = new GregorianCalendar(date.year, date.month-1, 1);

        int firstDay = tempCal.get(GregorianCalendar.DAY_OF_WEEK);

        return firstDay;

    }

}
