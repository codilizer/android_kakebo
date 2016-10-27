package jp.codilizer.android.kakebo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by codilizer on 2016/04/24.
 */
public class SquareRelativeLayout extends RelativeLayout {
    public SquareRelativeLayout(Context context)
    {
        super(context, null);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
