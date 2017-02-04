package lin.demo.binding;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import lin.core.ToolsActivity;
import lin.demo.R;

/**
 * Created by lin on 16/01/2017.
 */

public class BindViewActivity extends ToolsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_act);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}