package jp.codilizer.android.kakebo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by codilizer on 2016/04/29.
 */
public class ListExpenseAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<ExpenseItem> mArraySrc;
    int mLayout;

    public ListExpenseAdapter(Context context, int aLayout, ArrayList<ExpenseItem> aArrSrc) {
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

        mArraySrc = aArrSrc;
        mLayout = aLayout;
    }

    public int getCount() {
        return mArraySrc.size();
    }
    public String getItem(int position) {
        return mArraySrc.get(position).mExpending;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        if(convertView == null) {
            convertView = mInflater.inflate(mLayout, parent, false);
        }

        TextView tvExpenseName = (TextView)convertView.findViewById(jp.codilizer.android.kakebo.R.id.expense_item_name);
        tvExpenseName.setText(mArraySrc.get(position).mExpenseName);

        TextView tvExpenseIncome = (TextView)convertView.findViewById(jp.codilizer.android.kakebo.R.id.expense_income);
        tvExpenseIncome.setText(mArraySrc.get(position).mIncome);

        TextView tvExpenseExpending = (TextView)convertView.findViewById(jp.codilizer.android.kakebo.R.id.expense_expending);
        tvExpenseExpending.setText(mArraySrc.get(position).mExpending);

        return convertView;
    }
}
