package lin.core.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lin on 10/01/2017.
 */

public abstract class PtrViewBase<T extends View> extends PtrDefaultView {

    public PtrViewBase(Context context) {
        super(context);
        this.init();
    }

    public PtrViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public PtrViewBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init(){
        View view = this.genContentView();
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if(lp == null){
            view.setLayoutParams(new PtrView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        }else {
            if (!(lp instanceof PtrView.LayoutParams)) {
                view.setLayoutParams(new PtrView.LayoutParams(lp));
            }
        }
        this.setContentView(view);
    }

    protected abstract T genContentView();

    @Override
    public T getView() {
        return (T) super.getView();
    }
}
