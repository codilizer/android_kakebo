package jp.codilizer.android.kakebo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by codilizer on 2016/04/24.
 */


public class DetailActivity extends AppCompatActivity {

    private TextView title;
    ArrayList<Account> mAccountList;
    ArrayList<ExpenseItem> mExpensesList;
    ListExpenseAdapter mListExpensesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(jp.codilizer.android.kakebo.R.layout.activity_detail);

        Intent myIntent = getIntent();
        String currentDateString = myIntent.getStringExtra("CurrentDate");

        title = (TextView) findViewById(jp.codilizer.android.kakebo.R.id.detail_title);

        displayTitle();

        refreshExpensesList();

        RelativeLayout previousDay = (RelativeLayout) findViewById(jp.codilizer.android.kakebo.R.id.detail_previous);

        previousDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousDay();
                displayTitle();
            }
        });

        RelativeLayout nextDay = (RelativeLayout) findViewById(jp.codilizer.android.kakebo.R.id.detail_next);
        nextDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextDay();
                displayTitle();
            }
        });
    }

    @Override
    public void onResume() {
        Log.d("[DEBUG] ", "Activity::onResume() called! ");
        super.onResume();

        refreshExpensesList();

        mListExpensesAdapter.notifyDataSetChanged();
    }

    protected void setPreviousDay() {
        //month.add(Calendar.DAY_OF_MONTH, -1);
        DateManager.getInstance().movePreviousDay();
        refreshExpensesList();
    }

    protected void setNextDay() {
        //month.add(Calendar.DAY_OF_MONTH, 1);
        DateManager.getInstance().moveNextDay();
        refreshExpensesList();
    }

    protected void displayTitle() {
        String strTitle = Util.getInstance().DetailTitle(DateManager.getInstance().getCurCalendar());
        title.setText(strTitle);
    }

    public void mOnClickBtnBack(View v) {
        finish();
    }

    public void mOnClickBtnEdit(View v) {
        Intent intent = new Intent(this, EditActivity.class);
        //intent.putExtra("CurrentDate", adapter.curentDateString);

        startActivity(intent);
    }

    public void refreshExpensesList() {
        mAccountList = AccountManager.getInstance().GetExpenseListByDay(
                DateManager.getInstance().getCurDateString());



        int sizeExpenseList  = mAccountList.size();
        mExpensesList = new ArrayList<ExpenseItem>();

        for(int i = 0; i < sizeExpenseList; i++) {

            Account acc = mAccountList.get(i);

            ExpenseItem ei;

            if(acc.accountDivision == Common.AccountDivision.income) {
                ei = new ExpenseItem(acc.expenseDivision,
                        String.valueOf(acc.amount), "0");
            } else if (acc.accountDivision == Common.AccountDivision.spending) {
                ei = new ExpenseItem(acc.expenseDivision,
                        "0", String.valueOf(acc.amount));
            } else {
                ei = new ExpenseItem("", "", "");
            }

            mExpensesList.add(ei);
        }

        mListExpensesAdapter = new ListExpenseAdapter(this, jp.codilizer.android.kakebo.R.layout.expenses_item, mExpensesList);

        ListView ExpenseList;
        ExpenseList=(ListView)findViewById(jp.codilizer.android.kakebo.R.id.detail_list);
        ExpenseList.setAdapter(mListExpensesAdapter);

        ExpenseList.setOnItemClickListener(mExpenseItemClickListner);

        int totalHeight = 0;
        for (int i = 0; i < mListExpensesAdapter.getCount(); i++) {
            View item = mListExpensesAdapter.getView(i, null, ExpenseList);
            item.measure(View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED);
            totalHeight += item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = ExpenseList.getLayoutParams();
        layoutParams.height = totalHeight + (ExpenseList.getDividerHeight() * (ExpenseList.getCount() - 1));
        ExpenseList.setLayoutParams(layoutParams);

    }

    AdapterView.OnItemClickListener mExpenseItemClickListner =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(DetailActivity.this, "Expense list : " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                }
            };
}