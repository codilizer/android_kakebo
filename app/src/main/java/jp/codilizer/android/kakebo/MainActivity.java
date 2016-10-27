package jp.codilizer.android.kakebo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    //public Calendar month, itemmonth;// calendar instances.
    public Calendar itemmonth;// calendar instances.
    //public static GregorianCalendar month;


    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker

    boolean isDaySpecified = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(jp.codilizer.android.kakebo.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(jp.codilizer.android.kakebo.R.id.main_toolbar);
        setSupportActionBar(toolbar);

        Locale.setDefault(Locale.JAPAN);

        DBAgent.getInstance().init(this);
        DateManager.getInstance().initDateManager();
        initDB();

        itemmonth = DateManager.getInstance().getCurCalendarClone();
        printMonth(DateManager.getInstance().getCurCalendar());

        items = new ArrayList<String>();
        adapter = new CalendarAdapter(this);

        GridView gridview = (GridView) findViewById(jp.codilizer.android.kakebo.R.id.main_gridview);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("[DEBUG] ", "gridview.setOnItemClickListener() onItemClick called! ");
            }
        });

        handler = new Handler();
        handler.post(calendarUpdater);



        TextView title = (TextView) findViewById(jp.codilizer.android.kakebo.R.id.main_title);
        String strTitle = Util.getInstance().CalendarTitle(DateManager.getInstance().getCurCalendar());

        title.setText(strTitle);

        refreshMonthlyExpense();
        refreshDailyExpense();

        RelativeLayout previous = (RelativeLayout) findViewById(jp.codilizer.android.kakebo.R.id.main_previous);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
                refreshDayTotalTitle();
                refreshDailyExpense();
                refreshMonthlyExpense();
            }
        });

        RelativeLayout next = (RelativeLayout) findViewById(jp.codilizer.android.kakebo.R.id.main_next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();
                refreshDayTotalTitle();
                refreshDailyExpense();
                refreshMonthlyExpense();
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Log.d("[DEBUG] ", "MainActivity::setOnItemClickListener() called! " );
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.
                if ((gridvalue > 10) && (position < 7)) {
                    setPreviousMonth();
                    refreshCalendar();
                    //((CalendarAdapter) parent.getAdapter()).setSelected(v);
                } else if ((gridvalue < 15) && (position > 27)) {
                    setNextMonth();
                    refreshCalendar();
                    //((CalendarAdapter) parent.getAdapter()).setSelected(v);
                }


                DateManager.getInstance().setCurDate(selectedGridDate);
                AccountManager.getInstance().SetCurrentDay(selectedGridDate);
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);

                refreshMonthlyExpense();
                refreshDayTotalTitle();
                refreshDailyExpense();

                //handler.post(calendarUpdater);

                //showToast(selectedGridDate);
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d("[DEBUG] ", "MainActivity::onSaveInstanceState() called! " );

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("[DEBUG] ", "MainActivity::onRestoreInstanceState() called! " );
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onResume(){
        Log.d("[DEBUG] ", "MainActivity::onResume() called! " );
        super.onResume();

        refreshCalendar();
        refreshDayTotalTitle();
        refreshMonthlyExpense();
        refreshDailyExpense();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(jp.codilizer.android.kakebo.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == jp.codilizer.android.kakebo.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setNextMonth() {
        printMonth(DateManager.getInstance().getCurCalendar());

        Log.d("[DEBUG] ", "MainActivity::setNextMonth() called! ");
        DateManager.getInstance().moveNextMonth();

        printMonth(DateManager.getInstance().getCurCalendar());
    }

    protected void setPreviousMonth() {
        printMonth(DateManager.getInstance().getCurCalendar());
        Log.d("[DEBUG] ", "MainActivity::setPreviousMonth() called! ");

        DateManager.getInstance().movePreviousMonth();

        printMonth(DateManager.getInstance().getCurCalendar());
    }

    protected void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    public void refreshCalendar() {
        TextView title = (TextView) findViewById(jp.codilizer.android.kakebo.R.id.main_title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(Util.getInstance().CalendarTitle(DateManager.getInstance().getCurCalendar()));
    }

    public void refreshDayTotalTitle() {
        TextView titleDayTotal = (TextView) findViewById(jp.codilizer.android.kakebo.R.id.main_day_total);
        titleDayTotal.setText(Util.getInstance().DayTotalTitle(DateManager.getInstance().getCurCalendar()));
    }

    public void refreshDailyExpense() {
        AccountSummary as = AccountManager.getInstance().GetDailyAccountSummary();

        TextView tvDailyIncome = (TextView)findViewById(jp.codilizer.android.kakebo.R.id.main_day_income_value);
        tvDailyIncome.setText(String.valueOf(as.totalIncome));

        TextView tvDailySpending = (TextView)findViewById(jp.codilizer.android.kakebo.R.id.main_day_spending_value);
        tvDailySpending.setText(String.valueOf(as.totalSpending));
    }

    public void refreshMonthlyExpense() {
        TextView tvMonthIncome = (TextView) findViewById(jp.codilizer.android.kakebo.R.id.main_month_income_value);
        tvMonthIncome.setText(String.valueOf(AccountManager.getInstance().GetMonthlyIncome()));

        TextView tvMonthSpending = (TextView) findViewById(jp.codilizer.android.kakebo.R.id.main_month_spending_value);
        tvMonthSpending.setText(String.valueOf(AccountManager.getInstance().GetMonthlySpending()));

        TextView tvMonthBalance = (TextView) findViewById(jp.codilizer.android.kakebo.R.id.main_month_balance_value);
        tvMonthBalance.setText(String.valueOf(AccountManager.getInstance().GetMonthBalance()));
    }


    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String itemvalue;
            for (int i = 0; i < 7; i++) {
                itemvalue = df.format(itemmonth.getTime());
                itemmonth.add(Calendar.DATE, 1);
                items.add("2012-09-12");
                items.add("2012-10-07");
                items.add("2012-10-15");
                items.add("2012-10-20");
                items.add("2012-11-30");
                items.add("2012-11-28");
                //items.add("2016-03-29");
            }

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };

    public void mOnClickBtnDetail(View v) {
        Intent intent = new Intent(this, DetailActivity.class);
        //intent.putExtra("CurrentDate", adapter.curentDateString);

        startActivity(intent);
    }

    public void mOnClickBtnEdit(View v) {
        Intent intent = new Intent(this, EditActivity.class);

        startActivity(intent);
    }

    public void printMonth(GregorianCalendar cal) {

        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        Log.d("[DEBUG] ", "MainActivity::printMonth() called! " );
        Log.d("[DEBUG] ", "Year : " + year + " Month : " + month + " DAY : " + dayOfMonth);

    }

    private void initDB() {
        SharedPreferences pref = getSharedPreferences("ApplicationPreferences", 0);

        String keyDBInit = "DBInit";

        String isDBInitialized = pref.getString(keyDBInit, "False");

        if(!isDBInitialized.equals("True") ) {
            // DB init
            Log.d("[DEBUG] ", "MainActivity::initDivisionList! " );
            DBAgent.getInstance().initDivisionList();

            SharedPreferences.Editor edit = pref.edit();

            edit.putString(keyDBInit, "True");
            edit.commit();
        }
    }
}
