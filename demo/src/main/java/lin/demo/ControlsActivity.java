package lin.demo;

import android.app.Activity;
import android.os.Bundle;

import lin.core.Segmented;
import lin.core.ViewActivity;

public class ControlsActivity extends ViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls);

        Segmented segmented = (Segmented) this.findViewById(R.id.segmentedId);

        segmented.addItems(new String[]{"第一项","第二项","第三项"});
    }

}
