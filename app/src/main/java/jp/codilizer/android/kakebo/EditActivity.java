package jp.codilizer.android.kakebo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by codilizer on 2016/04/28.
 */
public class EditActivity extends AppCompatActivity {


    private TextView title;
    //GregorianCalendar month;
    private Common.AccountDivision mCurAccountDivision;
    private EditText mMoneyAmount;
    private Spinner mSpinnerExpenseDivision;
    private EditText mEditMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(jp.codilizer.android.kakebo.R.layout.activity_edit);

        RelativeLayout previousDay = (RelativeLayout) findViewById(jp.codilizer.android.kakebo.R.id.edit_previous);

        mMoneyAmount = (EditText)findViewById(jp.codilizer.android.kakebo.R.id.edit_amount);
        mEditMemo = (EditText)findViewById(jp.codilizer.android.kakebo.R.id.edit_edit_memo);

        title = (TextView) findViewById(jp.codilizer.android.kakebo.R.id.edit_title);

        refreshTitle();

        previousDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousDay();
                refreshTitle();
            }
        });

        RelativeLayout nextDay = (RelativeLayout) findViewById(jp.codilizer.android.kakebo.R.id.edit_next);
        nextDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextDay();
                refreshTitle();
            }
        });

        setCurrentAccountDivision(Common.AccountDivision.spending);


        setSpinnerItem();

        mSpinnerExpenseDivision = (Spinner)findViewById(jp.codilizer.android.kakebo.R.id.spinner_expense_division);
        mSpinnerExpenseDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                //Spinner spinner = (Spinner) parent;
                //Cursor cursor = (Cursor)spinner.getSelectedItem();

                //int categoryId = cursor.getInt(cursor.getColumnIndex("CategoryID"));

                //Toast.makeText(this, Integer.valueOf(categoryId).toString(), Toast.LENGTH_LONG).show();

            }
            public void onNothingSelected(AdapterView parent) {
            }
        });
    }

    protected void setPreviousDay() {
        DateManager.getInstance().movePreviousDay();
    }

    protected void setNextDay() {
        DateManager.getInstance().moveNextDay();
    }

    protected void refreshTitle() {
        String strTitle = Util.getInstance().DetailTitle(DateManager.getInstance().getCurCalendar());
        title.setText(strTitle);
    }

    private void setCurrentAccountDivision(Common.AccountDivision ad) {
        mCurAccountDivision = ad;
        TextView tvSpending = (TextView) findViewById(jp.codilizer.android.kakebo.R.id.edit_label_spending);
        TextView tvIncome = (TextView) findViewById(jp.codilizer.android.kakebo.R.id.edit_label_income);

        if(Common.AccountDivision.spending == mCurAccountDivision) {
            tvSpending.setTextColor(Color.RED);
            tvIncome.setTextColor(Color.DKGRAY);

        } else if (Common.AccountDivision.income == mCurAccountDivision) {
            tvSpending.setTextColor(Color.DKGRAY);
            tvIncome.setTextColor(Color.BLUE);
        }
    }

    private void changeCurrentAccountDivision(){
        if(Common.AccountDivision.spending == mCurAccountDivision) {
            setCurrentAccountDivision(Common.AccountDivision.income);
        } else if (Common.AccountDivision.income == mCurAccountDivision) {
            setCurrentAccountDivision(Common.AccountDivision.spending);
        }

        setSpinnerItem();
    }

    private void setSpinnerItem() {
        ArrayList<String> spinnerArray = DBAgent.getInstance().getDivisionList(getCurrentAccountDivision());


        Spinner spinner = (Spinner)findViewById(jp.codilizer.android.kakebo.R.id.spinner_expense_division);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, jp.codilizer.android.kakebo.R.layout.spinner_expense_item, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

    }


    private Common.AccountDivision getCurrentAccountDivision() {
        return mCurAccountDivision;
    }

    public void mOnClickAccountDivision(View v) {

        Toast.makeText(this, "clicked A.. d..", Toast.LENGTH_SHORT).show();
        changeCurrentAccountDivision();
    }

    public void mOnClickBtnBack(View v) {
        finish();
    }

    public void mOnClickBtnDelete(View v) {

    }

    public void mOnClickBtnSave(View v) {
        // 編集モードと登録モードの区分が必要, まずは登録から。


        DBAgent.getInstance().saveExepense(getCurrentAccountDivision(),
                                            Integer.parseInt(mMoneyAmount.getText().toString()),
                                            DateManager.getInstance().getCurDateString(),
                                            mSpinnerExpenseDivision.getSelectedItem().toString(),
                                            mEditMemo.getText().toString());

        finish();

    }

}


