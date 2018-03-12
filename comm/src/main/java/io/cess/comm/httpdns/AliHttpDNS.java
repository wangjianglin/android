package io.cess.comm.httpdns;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.cess.util.MD5;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author lin
 * @date 4/25/16.
 */

public class AliHttpDNS extends AbstractHttpDNS{

    private static String SERVER_IP = "203.107.1.1";
    private static String SECRET_SERVER_IP = "203.107.1.1";
    private String mServerIp = SERVER_IP;
    private boolean mSetServerIp = false;
    private String mSecret;
    private boolean mIsSecret = false;
    private String mAccountId;

    private void setSecret(String secret){
        this.mSecret = secret;
        this.mIsSecret = this.mSecret != null && !"".equals(this.mSecret.trim());
        if(!mSetServerIp){
            if(mIsSecret){
                mServerIp = SECRET_SERVER_IP;
            }else{
                mServerIp = SERVER_IP;
            }
        }
    }

    public void setServerIp(String ip){
        mServerIp = ip;
        mSetServerIp = true;
    }

    public AliHttpDNS(Context context,String account){
        this(context,account,null);
    }

    public AliHttpDNS(Context context,String account,String secret){
        super(context);
        this.mAccountId = account;
        this.setSecret(secret);
    }

    @Override
    protected HostObject fetch(String hostName,int timeout) {
        if(mIsSecret){
            return fetchWithSecret(hostName,timeout);
        }
        return fetchWithoutSecret(hostName,timeout);
    }

    public HostObject fetchWithSecret(String hostName,int timeout){

        String time = (new Date().getTime() / 1000 + 60)+"";
        String md5 = MD5.digest(hostName+"-" + mSecret +"-" + time);
        String resolveUrl = "https://" + mServerIp + "/" + mAccountId + "/sign_d?host=" + hostName
                + "&t=" + time
                + "&s=" + md5;
        try {

            Request request = new Request.Builder()
                    .url(resolveUrl)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .readTimeout(timeout,TimeUnit.MILLISECONDS)
                    .writeTimeout(timeout,TimeUnit.MILLISECONDS)
                    .build();
            Response okResponse = okHttpClient.newCall(request).execute();
            if (okResponse.code() != 200) {
//                    HttpDNSLog.logW("[QueryHostTask.call] - response code: " + conn.getResponseCode());
            } else {
                return parse(okResponse.body().byteStream());
            }
        } catch (Exception e) {
            System.out.println();
//                if (HttpDNSLog.isLogEnabled()) {
//                    e.printStackTrace();
//                }
        }
        return null;
    }

    public HostObject fetchWithoutSecret(String hostName,int timeout){

    String resolveUrl = "https://" + mServerIp + "/" + mAccountId + "/d?host=" + hostName;
        try {

            Request request = new Request.Builder()
                    .url(resolveUrl)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .readTimeout(timeout,TimeUnit.MILLISECONDS)
                    .writeTimeout(timeout,TimeUnit.MILLISECONDS)
                    .build();
            Response okResponse = okHttpClient.newCall(request).execute();
            if (okResponse.code() != 200) {
//                    HttpDNSLog.logW("[QueryHostTask.call] - response code: " + conn.getResponseCode());
            } else {
                return parse(okResponse.body().byteStream());
            }
        } catch (Exception e) {
        }
        return null;
    }

    private HostObject parse(InputStream in) throws IOException, JSONException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = streamReader.readLine()) != null) {
            sb.append(line);
        }
        JSONObject json = new JSONObject(sb.toString());
        String host = json.getString("host");
        long ttl = json.getLong("ttl");
        JSONArray ips = json.getJSONArray("ips");
        if (host != null && ips != null && ips.length() > 0) {

            HostObject hostObject = new HostObject();

            List<String> ipsList = new ArrayList<>();

            String ipItem = null;
            for(int n=0;n<ips.length();n++){
                ipItem = ips.getString(n);
                if(ipItem == null || "".equals(ipItem)){
                    continue;
                }
                ipsList.add(ipItem);
            }

            hostObject.setHostName(host);
            hostObject.setTtl(ttl);
            hostObject.setIps(ipsList.toArray(new String[]{}));

            return hostObject;
        }
        return null;
    }

    private HostObject fetchWithHttpURLConnectionImpl(String hostName,int timeout) {
        String resolveUrl = "https://" + SERVER_IP + "/" + mAccountId + "/d?host=" + hostName;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(resolveUrl).openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            if (conn.getResponseCode() != 200) {
//                    HttpDNSLog.logW("[QueryHostTask.call] - response code: " + conn.getResponseCode());
            } else {
                return parse(conn.getInputStream());
            }
        } catch (Exception e) {
//                if (HttpDNSLog.isLogEnabled()) {
//                    e.printStackTrace();
//                }
        }
        return null;
    }
}

