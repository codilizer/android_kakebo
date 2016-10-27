package jp.codilizer.android.kakebo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by codilizer on 2016/04/23.
 */
public class CalendarAdapter extends BaseAdapter {
    private Context mContext;

    //private java.util.Calendar month;
    //public GregorianCalendar pmonth; // calendar instance for previous month
    /**
     * calendar instance for previous month for getting complete view
     */
    public GregorianCalendar pmonthmaxset;

    int firstDay;
    int maxWeeknumber;
    int maxP;
    int calMaxP;
    int lastWeekDay;
    int leftDays;
    int mnthlength;
    DateFormat df;

    private ArrayList<String> items;
    public static List<String> dayString;
    private View previousView;

    public ArrayList<AccountSummary> mArrayAccountSummary;

    public CalendarAdapter(Context c) {
        CalendarAdapter.dayString = new ArrayList<String>();
        //month = monthCalendar;

        mContext = c;
        //month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<String>();

        df = new SimpleDateFormat("yyyy-MM-dd");




        refreshDays();
    }

    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }

    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(jp.codilizer.android.kakebo.R.layout.calendar_item, null);

        }
        dayView = (TextView) v.findViewById(jp.codilizer.android.kakebo.R.id.date);
        // separates daystring into parts.
        String[] separatedTime = dayString.get(position).split("-");

        // taking last part of date. ie; 2 from 2012-12-02
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        // checking whether the day is in current month or not.
        if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
            // setting offdays to white color.
            dayView.setTextColor(Color.LTGRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if ((Integer.parseInt(gridvalue) < 14) && (position > 28)) {
            dayView.setTextColor(Color.LTGRAY);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        } else if (position % 7 == 6 ) { // saturday
            dayView.setTextColor(Color.BLUE);
        } else if (position % 7 == 0 ) { // sunday
            dayView.setTextColor(Color.RED);
        } else {
            // setting curent month's days in blue color.
            dayView.setTextColor(Color.BLACK);
        }

        if (dayString.get(position).equals(DateManager.getInstance().getCurDateString())) {
            setSelected(v);
            previousView = v;
            //v.setBackgroundColor(Color.RED);
            v.setBackgroundResource(jp.codilizer.android.kakebo.R.drawable.calendar_cel_selectl);

            AccountManager.getInstance().SetCurrentDay(position);


        } else {
            v.setBackgroundResource(jp.codilizer.android.kakebo.R.drawable.list_item_background);
            //v.setBackgroundColor(Color.TRANSPARENT);
        }
        dayView.setText(gridvalue);

        // create date string for comparison
        String date = dayString.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        //String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        String monthStr = "" + (DateManager.getInstance().getCurDate().month);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        // show icon if date is not empty and it exists in the items array
        ImageView iw = (ImageView) v.findViewById(jp.codilizer.android.kakebo.R.id.date_icon);
        if (date.length() > 0 && items != null && items.contains(date)) {
            iw.setVisibility(View.VISIBLE);
        } else {
            iw.setVisibility(View.INVISIBLE);
        }

        // Expense Item
        TextView tvIncome = (TextView)v.findViewById(jp.codilizer.android.kakebo.R.id.calendar_income);
        TextView tvSpeding = (TextView)v.findViewById(jp.codilizer.android.kakebo.R.id.calendar_spending);


        int income = mArrayAccountSummary.get(position).totalIncome;
        int spending = mArrayAccountSummary.get(position).totalSpending;

        if(income!=0){
            tvIncome.setText(String.valueOf(income));
        } else {
            tvIncome.setText("");
        }
        if(spending!=0){
            tvSpeding.setText(String.valueOf(spending));
        } else {
            tvSpeding.setText("");
        }

        return v;
    }

    public View setSelected(View view) {
        Log.d("[DEBUG] ", "CalendarAdapter::setSelected() called! ");
        if (previousView != null) {
//            LinearLayout layout = (LinearLayout)previousView.findViewById((R.id.cellbackground));
//
//            layout.setBackgroundResource(R.drawable.list_item_background);
            previousView.setBackgroundResource(jp.codilizer.android.kakebo.R.drawable.list_item_background);
            //previousView.setBackgroundColor(Color.RED);
        }
        previousView = view;

//        LinearLayout layout = (LinearLayout)previousView.findViewById((R.id.cellbackground));
//
//        layout.setBackgroundResource(R.drawable.list_item_background);
        view.setBackgroundResource(jp.codilizer.android.kakebo.R.drawable.calendar_cel_selectl);
        //view.setBackgroundColor(Color.RED);

        return view;
    }

    public void refreshDays() {
        Log.d("[DEBUG] ", "CalendarAdapter::refreshDays() called! ");

        // clear items
        items.clear();
        dayString.clear();
        //pmonth = (GregorianCalendar) month.clone();
        // month start day. ie; sun, mon, etc
        //firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);

        GregorianCalendar gCalendar = DateManager.getInstance().getCurCalendar();

        DateManager.getInstance().print(gCalendar);

        //firstDay = gCalendar.get(GregorianCalendar.DAY_OF_WEEK);

        firstDay = DateManager.getInstance().getFirstDay();

        // finding number of weeks in current month.
        //maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        //無条件６週で表示する
        maxWeeknumber = 6;

        // allocating maximum row number for the gridview.
        mnthlength = maxWeeknumber * 7;


        GregorianCalendar pmonth = getPmonth(DateManager.getInstance().getCurCalendarClone());

        maxP = getMaxP(pmonth); // previous month maximum day 31,30....



        calMaxP = maxP - (firstDay - 1);// calendar offday starting 24,25 ...
        /**
         * Calendar instance for getting a complete gridview including the three
         * month's (previous,current,next) dates.
         */
        //pmonthmaxset = (GregorianCalendar) gCalendar.clone();
        pmonthmaxset = DateManager.getInstance().cloneCalendar(pmonth);

        /**
         * setting the start date as previous month's required date.
         */
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP +1);

        DateManager.getInstance().print(pmonthmaxset);

        /**
         * filling calendar gridview.
         */
        int posCurrentDay = -1;

        for (int n = 0; n < mnthlength; n++) {

            String itemvalue = df.format(pmonthmaxset.getTime());
            pmonthmaxset.add(GregorianCalendar.DATE, 1);
            dayString.add(itemvalue);
            Log.d("[DEBUG] ", "n :" + n + "itemvalue : " + itemvalue );

            if(DateManager.getInstance().getCurDateString().equals(itemvalue)) {
                posCurrentDay = n;
            }
        }


        String strStartDayOfCalendar = dayString.get(0);
        String strEndDayOfCalendar = dayString.get(mnthlength-1);

        AccountManager.getInstance().refreshMonthlyAccount(dayString);
        mArrayAccountSummary = AccountManager.getInstance().getAccountSummaryMonthly();

        AccountManager.getInstance().SetCurrentDay(posCurrentDay);
    }


    private GregorianCalendar getPmonth(GregorianCalendar pmonth) {
        GregorianCalendar gCalendar = DateManager.getInstance().getCurCalendar();

        if (gCalendar.get(GregorianCalendar.MONTH) == gCalendar

                .getActualMinimum(GregorianCalendar.MONTH)) {


//            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
//                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
//
            pmonth.set((gCalendar.get(GregorianCalendar.YEAR) - 1),
                    gCalendar.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {

//            pmonth.set(GregorianCalendar.MONTH,
//                    month.get(GregorianCalendar.MONTH) - 1);

            pmonth.set(GregorianCalendar.MONTH,
                    gCalendar.get(GregorianCalendar.MONTH) - 1);
        }

        return pmonth;
    }

    private int getMaxP(GregorianCalendar pmonth) {
        Log.d("[DEBUG] ", "CalendarAdapter::getMaxP() called! ");

        maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        return maxP;
    }
}
