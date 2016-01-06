package lin.core;

import android.app.Application;
import android.net.Uri;
import android.test.ApplicationTestCase;

import org.apache.http.impl.client.SystemDefaultCredentialsProvider;

import lin.util.Procedure;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {

        super(Application.class);

        UpdateManager.UpdateInfo updateInfo = UpdateManager.updateInfo(this.getContext(), "https://www.feicuibaba.com/proxy/proxy.php?version=new&android=true&channel=own");
        if(updateInfo == null){
            return;
        }
        //ProcedureUri procedureUri = new ProcedureUri();
//        procedureUri.flag = updateInfo.getFlag();
//        procedureUri.message = updateInfo.getMessage();

        Procedure<Uri> procedure = new Procedure<Uri>() {
            @Override
            public void procedure(Uri obj) {
                System.out.println(obj);
            }
        };
        UpdateManager.DownloadResult result = UpdateManager.downloadApk(this.getContext(), "https://www.feicuibaba.com/proxy/proxy-channel.apk.php?channel=own", "buyer.apk", updateInfo.getVersion(), procedure);

        System.out.println(result);
    }
}