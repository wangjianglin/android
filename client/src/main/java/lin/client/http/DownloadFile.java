package lin.client.http;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lin.util.Utils;

/**
 * Created by lin on 9/24/15.
 */
class DownloadFile implements Aboutable {

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 10,
            TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    HttpClient http;
    private ProgressResultListener listener;
    private HttpGet get;
    Context context;

    DownloadFile(ProgressResultListener listener){
        this.listener = listener;
    }

    public void download(String url) {
        HttpGet get = new HttpGet(url);
        downloadImpl(get);
    }

    public void download(URI url) {
        HttpGet get = new HttpGet(url);
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
                    Error error = new Error();
                    error.setStackTrace(Utils.printStackTrace(e));
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
                        listener.fault(new Error());
                    }
                    listener.result(file,null);
                } else {
                    if(listener!=null){
                        listener.fault(new Error());
                    }
//                    throw new RuntimeException("下载失败，服务器连接异常，状态码:" + response.getStatusLine().getStatusCode());
                }
            }
        };
        executor.execute(task);
    }
}
