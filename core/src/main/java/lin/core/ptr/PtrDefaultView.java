package lin.core.ptr;

import android.content.Context;
import android.util.AttributeSet;

import lin.core.ptr.ui.PtrDefaultFooter;
import lin.core.ptr.ui.PtrDefaultHeader;

/**
 * Created by lin on 10/01/2017.
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
        if(this.getHeaderView() == null) {
            PtrDefaultHeader header = new PtrDefaultHeader(this.getContext());
            this.setHeaderView(header);
            this.addRefreshUIHandler(header);
        }

        if(this.getFooterView() == null) {
            PtrDefaultFooter footer = new PtrDefaultFooter(this.getContext());
            this.setFooterView(footer);
            this.addLoadMoreUIHandler(footer);
        }

        super.onFinishInflate();
    }
}
