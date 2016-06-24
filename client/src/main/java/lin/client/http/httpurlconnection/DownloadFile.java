package lin.client.http.httpurlconnection;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import lin.client.http.Error;
import lin.client.http.FileInfo;
import lin.client.http.HttpCommunicate;
import lin.client.http.HttpCommunicateDownloadFile;
import lin.client.http.HttpCommunicateImpl;
import lin.client.http.ProgressResultListener;

/**
 * Created by lin on 9/24/15.
 */
class DownloadFile implements HttpCommunicateDownloadFile {

    private static final int DOWNLOAD_SIZE = 800 * 1024;

    private ProgressResultListener listener;

    private HttpCommunicateImpl impl;
    private HttpCommunicate.Params params;
    private Context context;

    DownloadFile(Context context){
        this.context = context;
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
    public HttpFileInfo getFileInfo(URL url) {
        return getFileInfoImp(url,null);
    }

    private HttpFileInfo getFileInfoImp(URL url,Map<String,Boolean> urls){
        try {
            if (urls != null && urls.containsKey(url.toString())) {
                return null;
            }
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection conn = Utils.open(url.toString(),this.impl.getHttpDNS());

            conn.connect();


            int statusCode = conn.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || statusCode == HttpURLConnection.HTTP_MOVED_PERM) {
                if (urls == null) {
                    urls = new HashMap<String, Boolean>();
                }
                urls.put(url.toString(), true);
                return getFileInfoImp(new URL(conn.getHeaderField("Location")), urls);
            }

            HttpFileInfo info = new HttpFileInfo();
            info.setFileSize(conn.getContentLength());
            info.setLastModified(conn.getLastModified());

            return info;
        }catch (Throwable e){
            e.printStackTrace();
        }
        return null;

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

    public void download(URL url) {
//        HttpGet get = new HttpGet(url);
//        downloadImpl(url);
        boolean isSuccess = false;
        try{
//                    if ()
            isSuccess = downloadImpl(url);
        }catch (Throwable e){
            //有问题，异常有可能是 listener 中产生的
            e.printStackTrace();
//                    lin.client.http.Error error = new Error();
//                    error.setStackTrace(Utils.printStackTrace(e));
            Error error = new Error(-2,"","",lin.util.Utils.printStackTrace(e));
            listener.fault(error);
            return;
        }
        //FileInfo对象
        if(isSuccess) {
            listener.result(new FileInfo(url.toString(),file, fileName, lastModified), null);
        }else{
            Error error = new Error(-3,"","","");
            listener.fault(error);
        }
    }

    private int errorCode = 0;
    private long length = 0;
    private int rStart = 0;
    private int rEnd = 0;

    private long lastModified = 0;
    private File file;
    private String fileName;
    private int statusCode;

    //解决重复下载问题
    private boolean downloadImpl(final URL url)throws Throwable{


        String md5s = lin.util.MD5.digest(url.toString());

        File path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/cache");

        if (path == null) {
            path = new File(context.getCacheDir() + "/" + Environment.DIRECTORY_DOWNLOADS + "/cache");
        }

        //conn.getLastModified()
        String cacheFileName = path.getAbsoluteFile() + "/download-cache-" + md5s;// + (new Date()).getTime();// + "-" + conn.getLastModified();
        file = new File(cacheFileName + ".cache");
    //                file = File.createTempFile(md5s,".cache");


        if(file.exists()){
    //                    fileInfo(url, null);
            HttpFileInfo info = getFileInfo(url);
            length = info.getFileSize();
            lastModified = info.getLastModified();
            if(file.length() == length && file.lastModified() == lastModified){
                return true;
            }
            file.delete();
        }


        File dFile = new File(cacheFileName + ".download");
    //                File dFile = File.createTempFile(md5s,".download");

        path.mkdirs();

        byte[] buffer = new byte[1024 * 4];

        URL realUrl = url;
        do {
            realUrl = downFile(realUrl, dFile, buffer);
        }while ((statusCode == HttpURLConnection.HTTP_OK
                || statusCode == HttpURLConnection.HTTP_PARTIAL)
                && dFile.length() < length);

        //416 (Requested Range Not Satisfiable/请求范围无法满足)
        //416表示客户端包含了一个服务器无法满足的Range头信息的请求。该状态是新加入 HTTP 1.1的。
        if(statusCode == 416){//如果请求范围错误，重新下载
            dFile.delete();
            realUrl = url;
            do {
                realUrl = downFile(realUrl, dFile, buffer);
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
        if(length <= 0 || (length > 0 && dFile.length() == length)){
            dFile.renameTo(file);
            file.setLastModified(lastModified);
        }else{
            dFile.delete();
            return false;
        }

        return true;
    //                HttpURLConnection.setFollowRedirects(true);

    }


    private URL downFile(URL url,File dFile,byte[] buffer)throws Throwable {
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        HttpURLConnection conn = Utils.open(url.toString(),this.impl.getHttpDNS());
        conn.setRequestMethod("GET");
        //先禁gzip测试
    //                conn.setDoOutput(true);// 是否输入参数
    //                conn.setRequestMethod("POST");
        long start = dFile.length();
        if (start > 0) {
            conn.setRequestProperty("Range", "bytes=" + start + "-" + (start + DOWNLOAD_SIZE - 1));
        } else {
            start = 0;
            conn.setRequestProperty("Range", "bytes=" + start + "-" + (start + DOWNLOAD_SIZE - 1));
        }


    //                conn.setInstanceFollowRedirects(true);
        conn.connect();

        statusCode = conn.getResponseCode();

        if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                || statusCode == HttpURLConnection.HTTP_MOVED_PERM) {
            return downFile(new URL(conn.getHeaderField("Location")), dFile, buffer);
        }

        if (statusCode == HttpURLConnection.HTTP_OK) {
            length = conn.getContentLength();
            start = 0;
        } else if (statusCode == HttpURLConnection.HTTP_PARTIAL) {
            parseRange(conn.getHeaderField("Content-Range"));
        }

        lastModified = conn.getLastModified();
        fileName = parserFileName(conn.getHeaderField("Content-Disposition"));

        //if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
        if (statusCode == HttpURLConnection.HTTP_OK
                || statusCode == HttpURLConnection.HTTP_PARTIAL) {
    //                    final HttpEntity entity = response.getEntity();
    //                    final long length = entity.getContentLength();
    //                    InputStream _in = entity.getContent();

    //                    final int length = conn.getContentLength();

            OutputStream _out = new FileOutputStream(dFile, true);

            InputStream _in = conn.getInputStream();

            final boolean isGZIP = _in instanceof GZIPInputStream;
            GZIPInputStream _zin = null;
            if (isGZIP) {
                _zin = (GZIPInputStream) _in;
            }

            int count = 0;
            long total = start;
            while ((count = _in.read(buffer)) != -1) {
                _out.write(buffer, 0, count);

    //                        System.out.println("1 progress:"+total+"\ttotal:"+length);
                if (isGZIP) {
                    total += getLen(_zin);
                } else {
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
        return url;
    }

    private void parseRange(String range) {
        range = range.substring(6);
        String[] rs = range.split("/");

        length = Integer.parseInt(rs[1]);

        rs = rs[0].split("-");

        rStart = Integer.parseInt(rs[0]);
        rEnd = Integer.parseInt(rs[1]);
    }

    ////attachment; filename="buyers_own.apk"
    private String parserFileName(String value){

        if(value != null && value.length() > 23){
            return value.substring(22,value.length()-1);
        }
        return null;
    }

    private int getLen(GZIPInputStream in) {
        try {
            return lenField.getInt(in);
        } catch (IllegalAccessException e) {
        }
        return 0;
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