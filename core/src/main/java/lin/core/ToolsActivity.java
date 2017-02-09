//package lin.core;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.view.ViewGroup;
//
//import java.lang.ref.SoftReference;
//
///**
// * Created by lin on 15/01/2017.
// */
//
//public class ToolsActivity extends ViewActivity {
//
//    private boolean intercept = false;
//    @Override
//    protected void onCreate(final Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if(this.getSupportActionBar() == null){
//            super.setContentViewById(R.layout.lin_core_tools_act);
//            Toolbar toolbar = (Toolbar)this.findViewById(R.id.lin_core_tools_act_toolbar);
//            this.setSupportActionBar(toolbar);
//            intercept = true;
//        }
//    }
//
//    @Override
//    public void setContentView(int layoutResID) {
//        if(intercept){
//            View view= Views.loadView(this,layoutResID);
//            addContent(view);
//        }else {
//            super.setContentView(layoutResID);
//        }
//    }
//
//    @Override
//    public void setContentView(View view) {
//        if(intercept){
//            addContent(view);
//            Views.process(this, view);
//        }else {
//            super.setContentView(view);
//        }
//    }
//
//    @Override
//    public void setContentView(View view, ViewGroup.LayoutParams params) {
//        if(intercept){
//            addContent(view);
//            Views.process(this, view);
//        }else {
//            super.setContentView(view, params);
//        }
//    }
//
//    @Override
//    public void addContentView(View view, ViewGroup.LayoutParams params) {
//        if(intercept){
//            addContent(view);
//            Views.process(this, view);
//        }else {
//            super.addContentView(view, params);
//        }
//    }
//
//    private void addContent(View view) {
//        FragmentManager fragmentManager = this.getSupportFragmentManager();
//
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
////        if (item instanceof View) {
//        transaction.replace(R.id.lin_core_tools_act_layout, new ViewFragment(view));
////        } else {
////            transaction.replace(R.id.lin_core_nav_content_frame, (android.support.v4.app.Fragment) item);
////        }
//        transaction.commit();
//    }
//
//}
