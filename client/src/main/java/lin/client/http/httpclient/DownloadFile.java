package lin.client.http.httpclient;

/**
 * Created by lin on 1/10/16.
 */


import android.content.Context;
import android.os.Environment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lin.client.http.HttpCommunicate;
import lin.client.http.Error;
import lin.client.http.HttpCommunicateDownloadFile;
import lin.client.http.HttpCommunicateImpl;
import lin.client.http.ProgressResultListener;
import lin.util.Utils;

/**
 * Created by lin on 9/24/15.
 */

class DownloadFile implements HttpCommunicateDownloadFile {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 10,
            TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private HttpClient http;

    private ProgressResultListener listener;
    private HttpGet get;
    private Context context;
    private HttpCommunicate.Params params;
    private HttpCommunicateImpl impl;

    DownloadFile(HttpClient http){
        this.http = http;
    }

//    public void download(String url) {
//        HttpGet get = new HttpGet(url);
//        downloadImpl(get);
//    }

    public void download(URL url) {
        HttpGet get = new HttpGet(url.toString());
        downloadImpl(get);
    }


    public void abort(){
        if(get != null){
            get.abort();
        }
    }

    private void downloadImpl(final HttpGet get){
        this.get = get;
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try{
                    runImpl();
                }catch (Throwable e){
                    e.printStackTrace();
                    Error error = new Error(-3,null,null,Utils.printStackTrace(e));
//                    error.setStackTrace(Utils.printStackTrace(e));
                    listener.fault(error);
                }
            }

            private void runImpl()throws Throwable{

//                HttpGet get = new HttpGet();

                HttpResponse response = http.execute(get);

                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

                    final HttpEntity entity = response.getEntity();
                    final long length = entity.getContentLength();
                    InputStream _in = entity.getContent();

//                    File file = new File("");
                    String md5s = lin.util.MD5.digest(get.getURI().toString());


                    File file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/cache");

                    if (file == null) {
                        file = new File(context.getCacheDir() + "/" + Environment.DIRECTORY_DOWNLOADS + "/cache");
                    }

                    file = new File(file.getAbsoluteFile() + "/" + md5s + (new Date()).getTime() + ".cache");

                    OutputStream _out = new FileOutputStream(file);

                    byte[] buffer = new byte[1024 * 4];
                    int count = 0;
                    long total = 0;
                    while((count = _in.read(buffer)) != -1) {
                        _out.write(buffer, 0, count);
                        total += count;
//                        System.out.println("1 progress:"+total+"\ttotal:"+length);
                        listener.progress(total, length);
                    }
                    _in.close();
                    if (total != length) {
                        listener.fault(new Error(-3,null,null,null));
                    }
                    listener.result(file,null);
                } else {
                    if(listener!=null){
                        listener.fault(new Error(-3,null,null,null));
                    }
//                    throw new RuntimeException("下载失败，服务器连接异常，状态码:" + response.getStatusLine().getStatusCode());
                }
            }
        };
        executor.execute(task);
    }

    @Override
    public void setImpl(HttpCommunicateImpl impl) {
        this.impl = impl;
    }

    @Override
    public void setListener(ProgressResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void setParams(HttpCommunicate.Params params) {
        this.params = params;
    }
}
