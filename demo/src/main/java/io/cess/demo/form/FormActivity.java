package io.cess.demo.form;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.cess.core.LayoutInflaterFactory;
import io.cess.core.ptr.PtrView;
import io.cess.demo.R;
import io.cess.demo.databinding.ActivityFormBinding;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ptr_default);
        super.onCreate(savedInstanceState);
//        ActivityFormBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_form);
//        setContentView(R.layout.activity_form);
        ActivityFormBinding binding = ActivityFormBinding.inflate(LayoutInflaterFactory.from(this));

        FormData data = new FormData();
        data.setSegueCls(FormView.class);
        binding.setData(data);

        setContentView(binding.getRoot());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
