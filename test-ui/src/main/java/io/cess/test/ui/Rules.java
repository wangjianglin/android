package io.cess.test.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import io.cess.core.Nav;
import io.cess.core.NavActivity;


/**
 * Created by lin on 24/11/2017.
 *
 * 用于构建 Nav 测试用的 Activity，可以通过 Rules.result() 得到 Activity 的返回结果
 *
 */

public class Rules {

    /**
     * 表示返回的结果
     */
    private static Object[] mResult;
    /**
     * 表示是否有返回
     */
    private static boolean mIsResult = false;

    /**
     * 得到返回结果
     * @return
     */
    public static Object[] result(){
        return mResult;
    }

    public static boolean isResult(){
        return mIsResult;
    }

    /**
     * 构建 ActivityTestRule 对象，但并加载 Activity
     * @param cls
     * @param objs
     * @return
     */
    public static ActivityTestRule<NavActivity> testRuleWithLaunchActivity(Class<?> cls,Object ... objs){
        return testRuleImpl(cls,true,objs);
    }

    /**
     * 构建 ActivityTestRule 对象，但并不加载 Activity，需要手动调用 launchActivity 方法
     * @param cls
     * @param objs
     * @return
     */
    public static ActivityTestRule<NavActivity> testRule(Class<?> cls,Object ... objs){
        return testRuleImpl(cls,false,objs);
    }

    private static ActivityTestRule<NavActivity> testRuleImpl(Class<?> cls,boolean launchActivity,Object ... objs){

        mResult = null;
        mIsResult = false;

        final Activity mock = new Activity(){
            private Intent mIntent;
            @Override
            public Intent getIntent() {
                return mIntent;
            }

            @Override
            public void startActivity(Intent intent) {
                this.mIntent = intent;
            }

            @Override
            public String getPackageName() {
                return InstrumentationRegistry.getTargetContext().getPackageName();
            }
        };

        Nav nav = Nav.push(mock, cls, new Nav.Result() {
            @Override
            public void result(Object... result) {
                mIsResult = true;
                mResult = result;
            }
        }, objs);

        return new RuleCls<NavActivity>(nav,mock.getIntent(),NavActivity.class,false,launchActivity);
    }

    public static class RuleCls<T extends Activity> extends ActivityTestRule<T>{
        private Nav mNav;//持有对象，防止回收对象
        private Intent mIntent;
        private RuleCls(Nav nav,Intent intent,Class<T> activityClass, boolean initialTouchMode, boolean launchActivity) {
            super(activityClass, initialTouchMode, launchActivity);
            mIntent = intent;
            mNav = nav;
        }

        @Override
        protected Intent getActivityIntent() {
            return mIntent;
        }
    }
}
