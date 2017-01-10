package lin.core.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by lin on 10/01/2017.
 */

public class PtrListView extends PtrViewBase<ListView> {
    public PtrListView(Context context) {
        super(context);
    }

    public PtrListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ListView genContentView() {
        ListView listView = new ListView(this.getContext());
        listView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        return listView;
    }
}
