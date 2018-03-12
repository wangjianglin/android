package io.cess.core.ptr;

import android.content.Context;
import android.util.AttributeSet;

import io.cess.core.ptr.ui.PtrDefaultFooter;
import io.cess.core.ptr.ui.PtrDefaultHeader;

/**
 * @author lin
 * @date 10/01/2017.
 */

public class PtrDefaultView extends PtrView {
    public PtrDefaultView(Context context) {
        super(context);
    }

    public PtrDefaultView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrDefaultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate(){
        setDefHeaderAndFooter(this.getMode());
        super.onFinishInflate();
    }

    private void setDefHeaderAndFooter(Mode mode){
        if(this.getHeaderView() == null
                &&(mode == Mode.Refresh || mode == Mode.Both)) {
            PtrDefaultHeader header = new PtrDefaultHeader(this.getContext());
            this.setHeaderView(header);
            this.addRefreshUIHandler(header);
        }

        if(this.getFooterView() == null
                &&(mode == Mode.LoadMore || mode == Mode.Both)) {
            PtrDefaultFooter footer = new PtrDefaultFooter(this.getContext());
            this.setFooterView(footer);
            this.addLoadMoreUIHandler(footer);
        }
    }

    @Override
    public void setMode(Mode mode) {
        setDefHeaderAndFooter(mode);
        super.setMode(mode);
    }
}
