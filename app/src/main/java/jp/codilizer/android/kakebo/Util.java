package jp.codilizer.android.kakebo;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by codilizer on 2016/04/23.
 */
public final class Util {

    private static class UtilLoader {
        private static final Util INSTANCE = new Util();
    }

    private Util() {
        if (UtilLoader.INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static Util getInstance() {
        return UtilLoader.INSTANCE;
    }

    public String CalendarTitle(GregorianCalendar gCalendar)
    {
        Calendar cal = gCalendar;

        int year       = cal.get(Calendar.YEAR);
        int month      = cal.get(Calendar.MONTH);
        int day        = cal.get(Calendar.DAY_OF_MONTH);

        return String.valueOf(year) + "　年　" + String.valueOf(month+1) + "　月";
    }

    public String DetailTitle(GregorianCalendar gCalendar)
    {
        Calendar cal = gCalendar;

        int year       = cal.get(Calendar.YEAR);
        int month      = cal.get(Calendar.MONTH);
        int day        = cal.get(Calendar.DAY_OF_MONTH);


        return String.valueOf(year) + "　年　" + String.valueOf(month+1) + "　月　"
                + String.valueOf(day) + " 日";
    }

    public String DayTotalTitle(GregorianCalendar gCalendar)
    {
        int year       = gCalendar.get(Calendar.YEAR);
        int month      = gCalendar.get(Calendar.MONTH);
        int day        = gCalendar.get(Calendar.DAY_OF_MONTH);

        return String.valueOf(year) + "年" + String.valueOf(month+1) + "月"
                + String.valueOf(day) + "日";
    }

    public GregorianCalendar GetGregorianCalendar(String dateString) {

        String[] separatedTime = dateString.split("-");
        GregorianCalendar gCal = new GregorianCalendar(Integer.parseInt(separatedTime[0]),
                        Integer.parseInt(separatedTime[1])-1,
                        Integer.parseInt(separatedTime[2]));

        return gCal;
    }

}