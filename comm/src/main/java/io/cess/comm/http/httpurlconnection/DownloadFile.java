package io.cess.comm.http.httpurlconnection;

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

import io.cess.comm.http.AbstractHttpCommunicateDownloadFile;
import io.cess.comm.http.Error;
import io.cess.comm.http.FileInfo;
import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.HttpCommunicateDownloadFile;
import io.cess.comm.http.HttpCommunicateImpl;
import io.cess.comm.http.HttpUtils;
import io.cess.comm.http.ProgressResultListener;

/**
 * Created by lin on 9/24/15.
 */
public class DownloadFile extends AbstractHttpCommunicateDownloadFile {


    public DownloadFile(Context context){
        super(context);
    }

    @Override
    protected HttpFileInfo getFileInfoImpl(String url) {
        return getFileInfoImp(url,null);
    }

    private HttpFileInfo getFileInfoImp(String url,Map<String,Boolean> urls){
        try {
            if (urls != null && urls.containsKey(url.toString())) {
                return null;
            }
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            HttpURLConnection conn = Utils.open(url.toString(),this.mImpl.getHttpDNS());

            conn.connect();


            int statusCode = conn.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || statusCode == HttpURLConnection.HTTP_MOVED_PERM) {
                if (urls == null) {
                    urls = new HashMap<String, Boolean>();
                }
                urls.put(url.toString(), true);
                return getFileInfoImp(conn.getHeaderField("Location"), urls);
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

    public void abort(){
        if(conn != null){
            conn.disconnect();
        }
    }

    private static Map<String,Boolean> downloadList = new HashMap<String,Boolean>();

    private HttpURLConnection conn;

//    public void download(String url) {
////        HttpGet get = new HttpGet(url);
////        downloadImpl(url);
//        boolean isSuccess = false;
//        try{
//            isSuccess = downloadImpl(url);
//        }catch (Throwable e){
//            //有问题，异常有可能是 listener 中产生的
//            e.printStackTrace();
////                    lin.client.http.Error error = new Error();
////                    error.setStackTrace(Utils.printStackTrace(e));
//            Error error = new Error(-2,"","",io.cess.util.Utils.printStackTrace(e));
//            mListener.fault(error);
//            return;
//        }
//        //FileInfo对象
//        if(isSuccess) {
//            mListener.result(new FileInfo(url,file, fileName, lastModified), null);
//        }else{
//            Error error = new Error(-3,"","","");
//            mListener.fault(error);
//        }
//    }

    private int errorCode = 0;
    private long length = 0;
    private int rStart = 0;
    private int rEnd = 0;

    private long lastModified = 0;
    private File file;
    private String fileName;
    private int statusCode;

    private String mRealUrl;
    private String mOrigalUrl;

//    //解决重复下载问题
//    protected FileInfo downloadImpl(final String url)throws Throwable{
//
//
//        String md5s = io.cess.util.MD5.digest(url);
//
//        File path = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + "/cache");
//
//        if (path == null) {
//            path = new File(mContext.getCacheDir() + "/" + Environment.DIRECTORY_DOWNLOADS + "/cache");
//        }
//
//        //conn.getLastModified()
//        String cacheFileName = path.getAbsoluteFile() + "/download-cache-" + md5s;// + (new Date()).getTime();// + "-" + conn.getLastModified();
//        file = new File(cacheFileName + ".cache");
//    //                file = File.createTempFile(md5s,".cache");
//
//
//        if(file.exists()){
//    //                    fileInfo(url, null);
//            HttpFileInfo info = getFileInfo(url);
//            length = info.getFileSize();
//            lastModified = info.getLastModified();
//            if(file.length() == length && file.lastModified() == lastModified){
//                return new FileInfo(url,file, fileName, lastModified,length);
//            }
//            file.delete();
//        }
//
//
//        File dFile = new File(cacheFileName + ".download");
//    //                File dFile = File.createTempFile(md5s,".download");
//
//        path.mkdirs();
//
//        byte[] buffer = new byte[1024 * 4];
//
//        String realUrl = url;
//        do {
//            realUrl = downFile(realUrl, dFile, buffer);
//        }while ((statusCode == HttpURLConnection.HTTP_OK
//                || statusCode == HttpURLConnection.HTTP_PARTIAL)
//                && dFile.length() < length);
//
//        //416 (Requested Range Not Satisfiable/请求范围无法满足)
//        //416表示客户端包含了一个服务器无法满足的Range头信息的请求。该状态是新加入 HTTP 1.1的。
//        if(statusCode == 416){//如果请求范围错误，重新下载
//            dFile.delete();
//            realUrl = url;
//            do {
//                realUrl = downFile(realUrl, dFile, buffer);
//            }while ((statusCode == HttpURLConnection.HTTP_OK
//                    || statusCode == HttpURLConnection.HTTP_PARTIAL)
//                    && dFile.length() < length);
//        }
//
//    //                if((statusCode == HttpURLConnection.HTTP_OK
//    //                        || statusCode == HttpURLConnection.HTTP_PARTIAL)
//    //                        && length > 0 && dFile.length() == length){
//    //                    dFile.delete();
//    //                }else{
//    //                    //dFile.renameTo(file);
//    //                }
//        if(length <= 0 || (length > 0 && dFile.length() == length)){
//            dFile.renameTo(file);
//            file.setLastModified(lastModified);
//        }else{
//            dFile.delete();
//            throw new RuntimeException("文件损坏");
//        }
//
//        return new FileInfo(url,file, fileName, lastModified,length);
//    //                HttpURLConnection.setFollowRedirects(true);
//
//    }

//    @Override
//    protected FileInfo downFilePartial(String url, File dFile, byte[] buffer) {
//        return null;
//    }

    @Override
    protected int getStatusCode() {
        return statusCode;
    }


    @Override
    protected FileInfo downFilePartial(String url,File dFile,byte[] buffer)throws Throwable {
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if(mRealUrl != null){
            url = mRealUrl;
        }
        if(mOrigalUrl == null){
            mOrigalUrl = url;
        }
        HttpURLConnection conn = Utils.open(url,this.mImpl.getHttpDNS());
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
            mRealUrl = null;
            return downFilePartial(conn.getHeaderField("Location"), dFile, buffer);
        }

        if (statusCode == HttpURLConnection.HTTP_OK) {
            length = conn.getContentLength();
            start = 0;
        } else if (statusCode == HttpURLConnection.HTTP_PARTIAL) {
            parseRange(conn.getHeaderField("Content-Range"));
        }

        mRealUrl = url;

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
                mListener.progress(total, length);
            }
            _in.close();
        }
        return new FileInfo(mOrigalUrl,dFile, fileName,lastModified,length);
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