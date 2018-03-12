package io.cess.demo.binding;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import io.cess.core.ViewActivity;
import io.cess.demo.R;

/**
 * @author lin
 * @date 16/01/2017.
 */

public class BindViewHolderActivity extends ViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_holder_act);


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
