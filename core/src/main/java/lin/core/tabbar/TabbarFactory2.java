package lin.core.tabbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import lin.core.LayoutInflaterFactory;
import lin.core.TabBar;

/**
 * Created by lin on 30/12/2016.
 */

public class TabbarFactory2 extends LayoutInflaterFactory.AbsFactory2 {


    @Override
    protected String tag() {
        return "tabbar";
    }

    @Override
    protected View createView(View view,Context context, AttributeSet attributeSet) {
        TabBar tabBar = new TabBar(context,attributeSet);
        return tabBar;
    }

}
