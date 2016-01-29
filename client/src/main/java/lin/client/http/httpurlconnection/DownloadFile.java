package lin.client.http.httpurlconnection;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import lin.client.http.Aboutable;
import lin.client.http.Error;
import lin.client.http.FileInfo;
import lin.client.http.HttpCommunicate;
import lin.client.http.HttpCommunicateDownloadFile;
import lin.client.http.HttpCommunicateImpl;
import lin.client.http.ProgressResultListener;
import lin.client.http.ResultListener;
import lin.util.Utils;

/**
 * Created by lin on 9/24/15.
 */
class DownloadFile implements HttpCommunicateDownloadFile {

    private static final int DOWNLOAD_SIZE = 400*1024;

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 10,
            TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    private ProgressResultListener listener;

    private HttpCommunicateImpl impl;
    private HttpCommunicate.Params params;


    @Override
    public void setImpl(HttpCommunicateImpl impl) {
        this.impl = impl;
    }

    @Override
    public void setListener(ProgressResultListener listener) {
        this.listener = listener;
    }

    public void download(URL url) {
//        HttpGet get = new HttpGet(url);
        downloadImpl(url);
    }

    @Override
    public void setParams(HttpCommunicate.Params params) {
        this.params = params;
    }


    public void abort(){
        if(conn != null){
            conn.disconnect();
        }
    }

    private static Map<String,Boolean> downloadList = new HashMap<String,Boolean>();

    private HttpURLConnection conn;

    //解决重复下载问题
    private void downloadImpl(final URL url){
//        this.get = get;
        Runnable task = new Runnable() {

            private int errorCode = 0;
            private int length = 0;
            private int rStart = 0;
            private int rEnd = 0;

            private long lastModified = 0;
            private File file;
            private String fileName;
            private int statusCode;
            @Override
            public void run() {
                try{
//                    if ()
                    runImpl(url);
                }catch (Throwable e){
                    //有问题，异常有可能是 listener 中产生的
                    e.printStackTrace();
//                    lin.client.http.Error error = new Error();
//                    error.setStackTrace(Utils.printStackTrace(e));
                    Error error = new Error(-2,"","",Utils.printStackTrace(e));
                    listener.fault(error);
                    return;
                }
                //FileInfo对象
                listener.result(new FileInfo(file,fileName,lastModified),null);
            }

            private void runImpl(URL url)throws Throwable{

                String md5s = lin.util.MD5.digest(url.toString());

//                File path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/cache");
//
//                if (path == null) {
//                    path = new File(context.getCacheDir() + "/" + Environment.DIRECTORY_DOWNLOADS + "/cache");
//                }

                //conn.getLastModified()
//                String fileName = path.getAbsoluteFile() + "/" + md5s;// + (new Date()).getTime();// + "-" + conn.getLastModified();
//                File file = new File(fileName + ".cache");
                File file = File.createTempFile(md5s,"cache");


                if(file.exists()){
                    fileInfo(url, null);
                    if(file.length() == length && file.lastModified() == lastModified){
                        return;
                    }
                    file.delete();
                }


                //File dFile = new File(fileName + ".download");
                File dFile = File.createTempFile(md5s,"download");



                byte[] buffer = new byte[1024 * 4];

                do {
                    downFile(url, dFile, buffer);
                }while ((statusCode == HttpURLConnection.HTTP_OK
                        || statusCode == HttpURLConnection.HTTP_PARTIAL)
                        && dFile.length() < length);

                if(statusCode == 416){
                    dFile.delete();
                    do {
                        downFile(url, dFile, buffer);
                    }while ((statusCode == HttpURLConnection.HTTP_OK
                            || statusCode == HttpURLConnection.HTTP_PARTIAL)
                            && dFile.length() < length);
                }

//                if((statusCode == HttpURLConnection.HTTP_OK
//                        || statusCode == HttpURLConnection.HTTP_PARTIAL)
//                        && length > 0 && dFile.length() == length){
//                    dFile.delete();
//                }else{
//                    //dFile.renameTo(file);
//                }
                if(length > 0 && dFile.length() == length){
                    dFile.renameTo(file);
                    dFile.setLastModified(lastModified);
                }

//                HttpURLConnection.setFollowRedirects(true);

            }

            private void downFile(URL url,File file,byte[] buffer)throws Throwable{
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //先禁gzip测试
//                conn.setDoOutput(true);// 是否输入参数
//                conn.setRequestMethod("POST");
                long start = file.length();
                if(start > 0) {
                    conn.setRequestProperty("Range", "bytes="+start+"-"+(start + DOWNLOAD_SIZE-1));
                }
//                conn.setRequestProperty("Range", "bytes="+start+"-"+(start+300));


//                conn.setInstanceFollowRedirects(true);
                conn.connect();

                statusCode = conn.getResponseCode();

                if(statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                        || statusCode == HttpURLConnection.HTTP_MOVED_PERM){
                    downFile(new URL(conn.getHeaderField("Location")), file,buffer);
                }

                if (statusCode == HttpURLConnection.HTTP_OK){
                    length = conn.getContentLength();
                    start = 0;
                }else if(statusCode == HttpURLConnection.HTTP_PARTIAL){
                    parseRange(conn.getHeaderField("Content-Range"));
                }

                lastModified = conn.getLastModified();

                //if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                if(statusCode == HttpURLConnection.HTTP_OK
                        || statusCode == HttpURLConnection.HTTP_PARTIAL){
//                    final HttpEntity entity = response.getEntity();
//                    final long length = entity.getContentLength();
//                    InputStream _in = entity.getContent();

//                    final int length = conn.getContentLength();

                    OutputStream _out = new FileOutputStream(file,true);

                    InputStream _in = conn.getInputStream();

                    final boolean isGZIP = _in instanceof GZIPInputStream;
                    GZIPInputStream _zin = null;
                    if(isGZIP){
                        _zin = (GZIPInputStream) _in;
                    }

                    int count = 0;
                    long total = start;
                    while((count = _in.read(buffer)) != -1) {
                        _out.write(buffer, 0, count);

//                        System.out.println("1 progress:"+total+"\ttotal:"+length);
                        if(isGZIP){
                            total += getLen(_zin);
                        }else {
                            total += count;
                        }
                        listener.progress(total, length);
                    }
                    _in.close();
//                    if (length > 0 && total != length) {
//                        listener.fault(new Error());
//                        return;
//                    }
//                    dFile.renameTo(file);
//                    //file.setLastModified()
//                    dFile.setLastModified(conn.getLastModified());
//                    listener.result(file,null);
                }
//                else {
//                    if(listener!=null){
//                        listener.fault(new Error());
//                    }
////                    throw new RuntimeException("下载失败，服务器连接异常，状态码:" + response.getStatusLine().getStatusCode());
//                }
            }

            private void parseRange(String range){
                range = range.substring(6);
                String[] rs = range.split("/");

                length = Integer.parseInt(rs[1]);

                rs = rs[0].split("-");

                rStart = Integer.parseInt(rs[0]);
                rEnd = Integer.parseInt(rs[1]);
            }

            private void fileInfo(URL url,Map<String,Boolean> urls)throws Throwable{

                if(urls != null && urls.containsKey(url.toString())){
                    return;
                }
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.connect();


                int statusCode = conn.getResponseCode();
                if(statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                        || statusCode == HttpURLConnection.HTTP_MOVED_PERM){
                    if(urls == null){
                        urls = new HashMap<String, Boolean>();
                    }
                    urls.put(url.toString(),true);
                    fileInfo(new URL(conn.getHeaderField("Location")),urls);
                    return;
                }

                length = conn.getContentLength();
                lastModified = conn.getLastModified();
            }


            private int getLen(GZIPInputStream in){
                try {
                    return lenField.getInt(in);
                } catch (IllegalAccessException e) {
                }
                return 0;
            }

        };
        executor.execute(task);
    }

    private final static Field lenField;

    static {
        Field tmpField = null;
        try {
            tmpField = InflaterInputStream.class.getDeclaredField("len");
            tmpField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        lenField = tmpField;
    }

}