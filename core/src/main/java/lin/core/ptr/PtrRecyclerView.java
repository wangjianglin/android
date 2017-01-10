package lin.core.ptr;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by lin on 10/01/2017.
 */

public class PtrRecyclerView extends PtrViewBase<RecyclerView>{
    public PtrRecyclerView(Context context) {
        super(context);
    }

    public PtrRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected RecyclerView genContentView() {
        RecyclerView recyclerView = new RecyclerView(this.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        return recyclerView;
    }
}
