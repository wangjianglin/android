package io.cess.core;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import io.cess.core.annotation.ResCls;
import io.cess.core.annotation.ResId;
import io.cess.core.annotation.ViewById;

/**
 * Created by lin on 11/01/2017.
 */

//加指示器   左、中、右
@ResCls(R.class)
@ResId(id="io_cess_core_swipe")
public class SwipeView extends ResView{
    public SwipeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeView(Context context) {
        super(context);
    }

    @ViewById(id="io_cess_core_swipe_pager")
    private ViewPager viewPager;

    private SimplePageAdapter mAdapter = new SimplePageAdapter();

    @Override
    protected void onInited() {
        if(viewPager != null){
            viewPager.setAdapter(mAdapter);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        LinearLayout.LayoutParams lp = null;
        if(params == null){
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
        }else if(params instanceof LinearLayout.LayoutParams){
            lp = (LinearLayout.LayoutParams) params;
        }else {
            lp = new LinearLayout.LayoutParams(params);
        }
        child.setLayoutParams(lp);
        views.add(child);
        mAdapter.notifyDataSetChanged();
    }

    private List<View> views = new ArrayList<View>();

    private class SimplePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }
    }
}
