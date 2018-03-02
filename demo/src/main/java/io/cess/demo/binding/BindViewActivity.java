package io.cess.demo.binding;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

//import io.cess.core.ToolsActivity;
import io.cess.core.ViewActivity;
import io.cess.demo.R;

/**
 * Created by lin on 16/01/2017.
 */

public class BindViewActivity extends ViewActivity {

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
