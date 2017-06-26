package lin.demo.http;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import lin.comm.http.Error;
import lin.comm.http.ExceptionPackage;
import lin.comm.http.HttpCommunicate;
import lin.comm.http.ResultListener;
import lin.comm.http.TestPackage;
import lin.core.LayoutInflaterFactory;
import lin.core.ViewActivity;
import lin.demo.R;
import lin.demo.databinding.ActivityFormBinding;
import lin.demo.form.FormData;
import lin.demo.form.FormView;

public class HttpActivity extends ViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ptr_default);
        super.onCreate(savedInstanceState);
//        ActivityFormBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_form);
//        setContentView(R.layout.activity_form);

        setContentView(R.layout.activity_http);

        ExceptionPackage pack = new ExceptionPackage();
        pack.setWarning(true);
//        TestPackage pack = new TestPackage();

        pack.setData("测试数据！");

//        ExceptionPackage pack = new ExceptionPackage();

        try {
            //HttpCommunicate.setCommUrl(new URL("http://www.feicuibaba.com:8080"));
            HttpCommunicate.setCommUrl(new URL("http://192.168.0.102:8080/fcbb_b2b2c"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpCommunicate.setDebug(true);

        HttpCommunicate.request(pack, new ResultListener() {
            @Override
            public void result(Object obj, List<Error> warning) {
                System.out.println();
            }

            @Override
            public void fault(Error error) {
                System.out.println();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
