package lin.client.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import lin.client.tcp.*;

/**
 * Created by lin on 9/24/15.
 * 改用 HttpURLConnection 实现HTTP请求
 */
public class HttpURLConnectionRequest {

    //HttpURLConnection

    private URL url;

    private String cookie;
//    private lin.client.http.Package pack;

    public void request(){

        Runnable task = new Runnable() {
            @Override
            public void run() {
            }

            private void runImpl()throws Throwable{

//                URL url = HttpUtils.u
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();


                // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
// http正文内，因此需要设为true, 默认情况下是false;
                connection.setDoOutput(true);

// 设置是否从httpUrlConnection读入，默认情况下是true;
                connection.setDoInput(true);

// Post 请求不能使用缓存
                connection.setUseCaches(false);

// 设定传送的内容类型是可序列化的java对象
// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
                connection.setRequestProperty("Content-type", "application/x-java-serialized-object");

// 设定请求的方法为"POST"，默认是GET
                connection.setRequestMethod("POST");


                if(cookie != null && cookie.length() > 0){
                    connection.setRequestProperty("Cookie",cookie);
                }
// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
                connection.connect();

                OutputStream _out = connection.getOutputStream();
                _out.write("".getBytes());

                InputStream _in = connection.getInputStream();

                cookie = connection.getHeaderField("set-cookie");
            }
        };
        //HttpURLConnection connection = HttpURLConnection
    }
}
