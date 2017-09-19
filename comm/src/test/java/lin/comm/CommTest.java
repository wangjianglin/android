package lin.comm;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import lin.comm.http.HttpCommunicate;
import lin.comm.http.HttpCommunicateResult;
import lin.comm.http.TestPackage;

/**
 * Created by lin on 09/07/2017.
 */

public class CommTest {

    @Before
    public void init() throws MalformedURLException {
        HttpCommunicate.setCommUrl(new URL("https://s.feicuibaba.com"));
    }
    @Test
    public void testHttp(){

        TestPackage pack = new TestPackage();
        pack.setData("ok");

        HttpCommunicateResult<String> r = HttpCommunicate.request(pack);

        r.waitForEnd();

        System.out.println(r.getResult());
    }
}