package lin.test;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import lin.core.ViewActivity;
import lin.core.Views;
import lin.core.annotation.ViewById;
import lin.core.annotation.ViewModel;
import lin.test.binding.User;
//import lin.test.databinding.ActivityBindingBinding;

public class BindingActivity extends ViewActivity {

    @ViewById(R.id.binding_text_id)
    private TextView textView;

    @ViewModel
    private User user = new User("fei", "Liang", 27);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_binding);

//        ViewDataBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_binding);
//        ActivityBindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_binding);
//        System.out.println(binding.getClass().getName());

//        View view = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.activity_binding, null, false);

//        View view = Views.loadView(this.getApplicationContext(),R.layout.activity_binding);
//        this.setContentView(view);
//        user = new User("fei", "Liang", 27);
//        Views.processAnnotation(view,this);


//        Views.processAnnotation(this);
//        DataBindingUtil.bind()
//        ActivityBindingBinding binding = DataBindingUtil.bind(view);
//        User user = new User("fei", "Liang", 27);
//        binding.setUser(user);

//        binding.setVariable(lin.test.BR.user
//        binding.setVariable(R.

//        setContentView(R.layout.activity_binding);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
