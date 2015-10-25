package lin.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import lin.core.Segmented;

public class ControlsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls);

        Segmented segmented = (Segmented) this.findViewById(R.id.segmentedId);

        segmented.addItems(new String[]{"第一项","第二项","第三项"});
    }

}
