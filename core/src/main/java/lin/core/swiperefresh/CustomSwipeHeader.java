package lin.core.swiperefresh;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import lin.core.R;

/**
 * 自定义的下拉刷新的头部
 *
 * Created by wangyeming on 2016/8/14.
 */
public class CustomSwipeHeader extends FrameLayout implements SwipeRefreshHeader, View.OnTouchListener {

    private TextView vDesc;

    public CustomSwipeHeader(Context context) {
        this(context, null);
    }

    public CustomSwipeHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSwipeHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {


//        LayoutInflater.from(getContext()).inflate(R.layout.lin_core_swiperefresh_header, this, true);
//        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(getContext(), 70));
//        layoutParams.setMargins(0, 0, dp2px(getContext(), 10), 0);
//        setLayoutParams(layoutParams);
//        setPadding(0, 0, 0, dp2px(getContext(), 10));
//        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
//        setOnTouchListener(this);
//        vDesc = (TextView) findViewById(R.id.lin_core_swiperefresh_header_tv);

        vDesc = new TextView(this.getContext());
        vDesc.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        vDesc.setTextColor(0xff000000);
        vDesc.setText("下拉刷新");

        this.addView(vDesc);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(getContext(), 70));
        layoutParams.setMargins(0, 0, dp2px(getContext(), 10), 0);
        setLayoutParams(layoutParams);
        setPadding(0, 0, 0, dp2px(getContext(), 10));
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        setOnTouchListener(this);
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        if(isRefresh) {
            vDesc.setText("刷新中");
        } else {
            vDesc.setText("刷新完毕");
        }
    }

    @Override
    public void onDrag(float percent) {
        if(percent < 1) {
            vDesc.setText("↓继续下拉 ");
        } else {
            vDesc.setText("↑松手释放，下拉刷新");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //以下是用来处理下拉刷新的事件问题
        return true;
    }

    public static int dp2px(Context context, int dip) {
        Resources resources = context.getResources();
        int px = Math.round(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics()));
        return px;
    }
}
