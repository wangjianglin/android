package io.cess.comm.http.okhttp;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import io.cess.comm.http.AbstractHttpCommunicateDownloadFile;
import io.cess.comm.http.Error;
import io.cess.comm.http.FileInfo;
import io.cess.comm.http.HttpClientResponseImpl;
import io.cess.comm.http.HttpCommunicate;
import io.cess.comm.http.HttpCommunicateDownloadFile;
import io.cess.comm.http.HttpCommunicateImpl;
import io.cess.comm.http.HttpMethod;
import io.cess.comm.http.HttpPackage;
import io.cess.comm.http.HttpUtils;
import io.cess.comm.http.ProgressResultListener;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lin on 9/24/15.
 */
public class DownloadFile extends AbstractHttpCommunicateDownloadFile {


    public DownloadFile(Context context) {
        super(context);
    }


    @Override
    protected FileInfo downFilePartial(String url, File dFile, byte[] buffer)throws Throwable {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);


        //先禁gzip测试
        //                conn.setDoOutput(true);// 是否输入参数
        //                conn.setRequestMethod("POST");
        long start = dFile.length();
        if (start > 0) {
            requestBuilder.addHeader("Range", "bytes=" + start + "-" + (start + DOWNLOAD_SIZE - 1));
        } else {
            start = 0;
            requestBuilder.addHeader("Range", "bytes=" + start + "-" + (start + DOWNLOAD_SIZE - 1));
        }


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(1200,TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS)
                .build();
        Call call = okHttpClient.newCall(requestBuilder.build());

        HttpClientResponseImpl response = new HttpClientResponseImpl();

        Response okResponse = null;
        try {
            okResponse = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(io.cess.util.Utils.printStackTrace(e));
        }

        statusCode = okResponse.code();
        if (statusCode == 200) {
            length = okResponse.body().contentLength();
            start = 0;
        } else if (statusCode == 206) {
            parseRange(okResponse.header("Content-Range"));
        }

        lastModified = parserLastModified(okResponse.header("Last-Modified"));
        fileName = parserFileName(okResponse.header("Content-Disposition"));

        if (statusCode == 200
                || statusCode == 206) {

            OutputStream _out = new FileOutputStream(dFile, true);

            InputStream _in = okResponse.body().byteStream();

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
        FileInfo fileInfo = new FileInfo(url,dFile,fileName,lastModified,length);
        return fileInfo;
    }

    @Override
    protected int getStatusCode() {
        return statusCode;
    }

    public void abort(){
//        if(conn != null){
//            conn.disconnect();
//        }
    }

    private long length = 0;
    private int rStart = 0;
    private int rEnd = 0;

    private long lastModified = 0;
    private File file;
    private String fileName;
    private int statusCode;


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

    private long parserLastModified(String s){
        if(s == null || "".equals(s.trim())){
            return 0l;
        }
        try {
            Date date = new Date(s);
            return date.getTime();
        }catch (Throwable e){
            return 0;
        }
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

    @Override
    protected HttpFileInfo getFileInfoImpl(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Range", "bytes=0-0")
                .build();


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(1200,TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS)
                .build();
        Call call = okHttpClient.newCall(request);

        HttpClientResponseImpl response = new HttpClientResponseImpl();

        Response okResponse = null;
        try {
            okResponse = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(io.cess.util.Utils.printStackTrace(e));
        }

        if(!okResponse.isSuccessful()){
            throw new RuntimeException(okResponse.message());
        }
//        statusCode = okResponse.code();
        HttpFileInfo fileInfo = new HttpFileInfo();
        fileInfo.setFileName(parserFileName(okResponse.header("Content-Disposition")));
        if(okResponse.code() == 200) {
            fileInfo.setFileSize(okResponse.body().contentLength());
        }else if(okResponse.code() == 206){
            parseRange(okResponse.header("Content-Range"));
            fileInfo.setFileSize(length);
        }
        fileInfo.setLastModified(parserLastModified(okResponse.header("Last-Modified")));
        return fileInfo;
    }
}