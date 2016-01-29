package lin.client.http.httpurlconnection;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lin.client.Constants;
import lin.client.http.*;
import lin.client.http.Error;


/**
 * Created by lin on 1/8/16.
 */
class HttpURLConnectionRequestRunable implements Runnable {

    private byte[] buffer = new byte[4096];

    private HttpPackage pack;
    private ResultListener listener;
    private HttpCommunicateImpl impl;
    private SessionInfo sessionInfo;

    HttpURLConnectionRequestRunable(SessionInfo sessionInfo, HttpPackage pack,ResultListener listener,HttpCommunicateImpl impl){
        this.pack = pack;
        this.listener = listener;
        this.impl = impl;
        this.sessionInfo = sessionInfo;
    }
    @Override
    public void run() {

        try {
//            URL url = new URL(HttpUtils.uri(impl, pack));
            runImpl(HttpUtils.uri(impl, pack), false, null);
        } catch (Throwable e) {
            lin.client.http.Error error = new Error(-2,
                    "未知错误",
                    e.getMessage(),
                    lin.util.Utils.printStackTrace(e));
//            error.setCode(-2);
//            error.setMessage("未知错误");
//            error.setCause(e.getMessage());
//            error.setStackTrace(lin.util.Utils.printStackTrace(e));

            HttpUtils.fireFault(listener, error);
            return;
        }
        pack.getRequestHandle().response(pack, buffer, listener);
    }

    private void runImpl(String urlString, boolean redirect, List<String> urls) throws Throwable {

//                URL url = new URL(HttpUtils.uri(impl,pack));
//                URL url = HttpUtils.u
        if (pack.getMethod() != HttpMethod.POST && pack.isMultipart()) {
            throw new RuntimeException("Multipart必须采用post请求！");
        }

        if (pack.getMethod() == HttpMethod.GET) {
            String paramsString = generParams(pack.getParams());
            urlString = addGetParams(urlString, paramsString);
        }
        HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();

        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");

        Map<String, Object> params = pack.getRequestHandle().getParams(pack,new HttpURLConnectionMessage(conn));

        conn.setRequestMethod(pack.getMethod().name());
        conn.setConnectTimeout(impl.getTimeout());
        conn.setReadTimeout(impl.getTimeout());


        for (Map.Entry<String, String> item : impl.defaultHeaders().entrySet()) {
            conn.setRequestProperty(item.getKey(), item.getValue());
        }
        conn.setRequestProperty(Constants.HTTP_COMM_PROTOCOL, "");
        if (impl.isDebug()) {
            conn.setRequestProperty(Constants.HTTP_COMM_PROTOCOL_DEBUG, "");
        }

        // Post 请求不能使用缓存
        conn.setUseCaches(false);

// 设定传送的内容类型是可序列化的java对象
// (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
//                conn.setRequestProperty("Content-type", "application/x-java-serialized-object");

// 设定请求的方法为"POST"，默认是GET
//                conn.setRequestMethod("POST");


        if (sessionInfo.cookie != null && sessionInfo.cookie.length() > 0) {
            conn.setRequestProperty("Cookie", sessionInfo.cookie);
        }

        // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
// http正文内，因此需要设为true, 默认情况下是false;
        if (pack.getMethod() != HttpMethod.GET) {
            if (params != null && !params.isEmpty()) {
                conn.setDoOutput(true);
                conn.setDoInput(true);
                if (pack.isMultipart()) {
                    setMultipartParams(conn,params);
                } else {
                    setPostParams(conn, params);
                }
            }
        } else {
            conn.setDoOutput(false);
        }


// 设置是否从httpUrlConnection读入，默认情况下是true;
//                conn.setDoInput(true);


// 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
        conn.connect();

        int statusCode = conn.getResponseCode();

        if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                || statusCode == HttpURLConnection.HTTP_MOVED_PERM) {//请求重定向
            if (urls == null) {
                urls = new ArrayList<String>();
            }
            urls.add(urlString);
            urlString = conn.getHeaderField("Location");
            if (urls.contains(urlString)) {
                throw new RuntimeException();
            }
            runImpl(urlString, true, urls);
            return;
        }

//                OutputStream _out = conn.getOutputStream();

        readData(conn);

        conn.disconnect();
    }

    private void readData(HttpURLConnection conn) throws Throwable {
        int count = 0;

        ByteArrayOutputStream _out = new ByteArrayOutputStream();
        InputStream _in = conn.getInputStream();
        while ((count = _in.read(buffer)) != -1) {
            _out.write(buffer, 0, count);
        }

        String cookie = conn.getHeaderField("set-cookie");

        if(cookie != null && !"".equals(cookie)) {
            sessionInfo.cookie = cookie;
        }

        buffer = _out.toByteArray();
        _in.close();
    }

    private void setPostParams(HttpURLConnection conn, Map<String, Object> params) throws Throwable {
        conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

        String paramsString = generParams(params);
        conn.setRequestProperty("Content-Length", String
                .valueOf(paramsString.length()));
        // 发送POST请求必须设置如下两行
//                conn.setDoOutput(true);
//                conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        printWriter.write(paramsString);
        // flush输出流的缓冲
        printWriter.flush();
    }

//            private void setGetParams(HttpURLConnection conn,Map<String,Object> params){
//
//            }

    private String addGetParams(String urlString, String params) throws Throwable {
        if (urlString.indexOf('?') == -1) {
            urlString += "?" + params;
        } else {
            urlString += "&" + params;
        }
        return urlString;
    }

    private String generParams(Map<String, Object> params) throws Throwable {

        if (params == null) {
            return "";
        }
        StringBuffer sBuffer = new StringBuffer();
        for (Map.Entry<String, Object> item : params.entrySet()) {
            sBuffer.append(item.getKey());
            sBuffer.append("=");
            if (item.getValue() != null) {
                sBuffer.append(encode(item.getValue().toString()));

            }
            sBuffer.append("&");
        }
        if (sBuffer.length() > 0) {
            sBuffer.deleteCharAt(sBuffer.length() - 1);
        }
        return sBuffer.toString();
    }

//    private String generGetParams(Map<String, Object> params) throws Throwable {
//
//        if (params == null) {
//            return "";
//        }
//        StringBuffer sBuffer = new StringBuffer();
//        for (Map.Entry<String, Object> item : params.entrySet()) {
//            sBuffer.append(item.getKey());
//            sBuffer.append("=");
//            if (item.getValue() != null) {
//                sBuffer.append(item.getValue().toString());
//            }
//            sBuffer.append("&");
//        }
//        if (sBuffer.length() > 0) {
//            sBuffer.deleteCharAt(sBuffer.length() - 1);
//        }
////                return sBuffer.toString();
//        return URLEncoder.encode(sBuffer.toString(), "utf-8");
//    }

    private void setMultipartParams(HttpURLConnection conn, Map<String, Object> params) throws Throwable {
//        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);

        Map<String,FileParamInfo> fileParams = new HashMap<String,FileParamInfo>();
        Map<String,String> textParams = new HashMap<String,String>();

        FileParamInfo fileValue = null;
        String textValue = null;

        boolean isLength = true;
        long length = 0;
        String key = null;
        for(Map.Entry<String,Object> item : params.entrySet()){
            if(item.getValue() instanceof FileParamInfo){
                fileValue = (FileParamInfo)item.getValue();
                fileParams.put(item.getKey(),fileValue);

                if(fileValue.getLength() < 0){
                    isLength = false;
                }
//                length += fileValue.getLength();
                length += boundaryBytes.length + cdBytes.length + item.getKey().length() + fnBytes.length + fileValue.getFileName().length()+cdlfBytes.length
                        + ctBytes.length + fileValue.getMimeType().length() + lfBytes.length + fileValue.getLength() + lfBytes.length;
            }else{
                if(item.getValue() == null){
                    textValue = "";
                }else {
                    textValue = encode(item.getValue().toString());
                }

                key = encode(item.getKey());
                textParams.put(key,textValue);

                length += textValue.length() + boundaryBytes.length + cdBytes.length + key.length() + cdlfBytes.length + lfBytes.length;
            }
        }
        if(isLength){

            length += boundaryBytes.length + endBytes.length;
            conn.setRequestProperty("Content-Length", String
                    .valueOf(length));
        }

        OutputStream out = conn.getOutputStream();

        writeStringParams(out,textParams);
        writeFileParams(out, fileParams);
        paramsEnd(out);
    }

//    private String boundary = "--------httppost123";
    private static String boundary = "---------------------------7d33a816d302b6";
//    private static int boundaryLength = boundary.length();
    private static byte[] boundaryBytes = ("--" + boundary + "\r\n").getBytes();
    private static byte[] lfBytes = "\r\n".getBytes();
    private static byte[] cdBytes = "Content-Disposition: form-data; name=\"".getBytes();
    private static byte[] cdlfBytes = "\"\r\n".getBytes();

    private static byte[] fnBytes = "\"; filename=\"".getBytes();
    private static byte[] ctBytes = "Content-Type: ".getBytes();

    private static byte[] endBytes = ("--" + boundary + "--" + "\r\n\r\n").getBytes();

    //普通字符串数据
    private void writeStringParams(OutputStream out,Map<String,String> params) throws Exception {

        for (Map.Entry<String,String> item : params.entrySet()) {
//            out.write(("--" + boundary + "\r\n").getBytes());
            out.write(boundaryBytes);
//            out.write("Content-Disposition: form-data; name=\"" + item.getKey()
//                    + "\"\r\n");
            out.write(cdBytes);
            out.write(item.getKey().getBytes());
            out.write(cdlfBytes);
//            out.write("\r\n");
//            out.write(item.getValue() + "\r\n");
            out.write(item.getValue().getBytes());
            out.write(lfBytes);
        }
    }
    //文件数据
    private void writeFileParams(OutputStream out,Map<String,FileParamInfo> params) throws Throwable {

        for (Map.Entry<String,FileParamInfo> item : params.entrySet()) {

//            length += boundaryBytes.length + cdBytes.length + item.getKey().length() + fnBytes.length + item.getValue().getFileName().length()+cdlfBytes.length
//                    + ctBytes.length + item.getValue().getMimeType().length() + lfBytes.length + item.getValue().getLength() + lfBytes.length;
//            ds.writeBytes("--" + boundary + "\r\n");
            out.write(boundaryBytes);
//            ds.writeBytes("Content-Disposition: form-data; name=\"" + item.getKey()
//                    + "\"; filename=\"" + encode(item.getValue().getFileName()) + "\"\r\n");
            out.write(cdBytes);
            out.write(item.getKey().getBytes());
            out.write(fnBytes);
            out.write(encode(item.getValue().getFileName()).getBytes());
            out.write(cdlfBytes);


//            ds.writeBytes("Content-Type: " + item.getValue().getMimeType() + "\r\n");

            out.write(ctBytes);
            out.write(encode(item.getValue().getMimeType()).getBytes());
            out.write(lfBytes);

//            ds.writeBytes("\r\n");
            out.write(lfBytes);
            //ds.write(getBytes(value));
            InputStream _in = item.getValue().getFile();
            //_in.
            int count = 0;
            while ((count = _in.read(buffer)) != -1){
                out.write(buffer,0,count);
            }
//            out.write("\r\n".getBytes());
            out.write(lfBytes);
            out.flush();
        }
    }

    //添加结尾数据
    private void paramsEnd(OutputStream out) throws Throwable {
//        ds.writeBytes("--" + boundary + "--" + "\r\n");
//        ds.writeBytes("\r\n");
//        out.write(("--" + boundary + "--" + "\r\n\r\n").getBytes());
//        out.write(boundaryBytes);
//        out.write(lfBytes);
        out.write(endBytes);
    }

    private String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }

}
