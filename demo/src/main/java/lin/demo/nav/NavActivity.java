package lin.demo.nav;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lin.core.Nav;
import lin.core.ViewActivity;
import lin.core.ViewActivity;
import lin.core.annotation.Click;
import lin.demo.R;

public class NavActivity extends ViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        this.setTitle("nav");
    }

    @Click(R.id.nav_act_push_button)
    private void pushClick(){
        Nav.push(this,NavView.class,new Nav.Result(){
            @Override
            public void result(Object... result) {
                System.out.println("===========================");
            }
        });
    }

    @Click(R.id.nav_act_push_frag_button)
    private void pushFragClick(){
        Nav.push(this,NavFragment.class,new Nav.Result(){
            @Override
            public void result(Object... result) {
                System.out.println("***************************");
            }
        });
    }

    @Click(R.id.nav_act_push_layout_button)
    private void pushLayoutIdClick(){
        Nav.push(this,R.layout.activity_nav_layout_id,new Nav.Result(){
            @Override
            public void result(Object... result) {
                System.out.println("***************************");
            }
        });
    }


    @Click(R.id.nav_act_push_view_holder_button)
    private void pushViewHolderClick(){
        Nav.push(this,NavViewHolder.class,new Nav.Result(){
            @Override
            public void result(Object... result) {
                System.out.println("***************************");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
