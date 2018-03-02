package lin.comm.http.auth;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import lin.util.ThreadUtil;

/**
 * Created by lin on 14/01/2018.
 */

public class OAuth2Authentication implements Authentication {

    private class InMoneryOAuth2TokenStore implements OAuth2TokenStore{

        @Override
        public void store(OAuth2Token token) {

        }

        @Override
        public OAuth2Token load() {
            return null;
        }
    }

    private BaseAuthentication base = new BaseAuthentication();
    public String clientId;
    public String secret;
    public String username;
    public String password;

    private OAuth2GrantType grantType;

    public String scope = "";

    private String tokenUrl;
    private String refreshUrl;

    public OAuth2TokenStore tokenStore = new InMoneryOAuth2TokenStore();

    private OAuth2Token token;

    private boolean refreshing = false;

    public OAuth2Authentication(String tokenUrl){
        this(tokenUrl,tokenUrl);
    }
    public OAuth2Authentication(String tokenUrl,String refreshUrl){
        this.tokenUrl = tokenUrl;
        this.refreshUrl = refreshUrl;
        if(refreshUrl == null || "".equals(refreshUrl)){
            this.refreshUrl = tokenUrl;
        }
    }

    public String auth() {
        final OAuth2Token token = this.token;
        if(token != null) {
            if(token.getExpiresIn() < new Date().getTime() / 1000) {
                ThreadUtil.asynQueue(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (token.getType() == OAuth2GrantType.Password) {
                                refreshToken();
                            } else {
                                getToken(OAuth2GrantType.Client,20*1000);
                            }
                        }catch (Throwable e){}
                    }
                });
            }
            return token.getTokenType() + " " + token.getAccessToke();
        }
        try {
            getToken(this.grantType,5*1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        OAuth2Token resultToken = this.token;
        if(resultToken != null){
            return resultToken.getTokenType() + " " + resultToken.getAccessToke();
        }
        return "";
    }

    private void refreshToken() throws IOException {
        if(refreshing){
            return;
        }
        synchronized (this) {
            if(refreshing){
                return;
            }
            refreshing = true;
        }

        try {
            tokenImpl(OAuth2GrantType.Password,
                    "grant_type=refresh_token&refresh_token=" + token.getRefreshToken(),
                    20 * 1000
            );
        }finally {
            synchronized (this) {
                refreshing = false;
            }
        }
    }
    private void tokenImpl(OAuth2GrantType type, String queryString,int timeout) throws IOException {
        URL url = new URL(tokenUrl);

        HttpURLConnection conn = (HttpURLConnection) new URL(tokenUrl).openConnection();
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setRequestProperty("Authorization",base.auth());
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Length", String
                .valueOf(queryString.length()));
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        printWriter.write(queryString);
        // flush输出流的缓冲
        printWriter.flush();
        if (conn.getResponseCode() != 200) {
            this.setToken(null);
        } else {
            InputStream in = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }

            this.setToken(OAuth2Token.loadToken(sb.toString(),type));
        }
//            JSONObject json = new JSONObject(sb.toString());
//            String host = json.getString("host");
//            long ttl = json.getLong("ttl");
//        var request = URLRequest(url: url!, cachePolicy: .reloadIgnoringLocalAndRemoteCacheData, timeoutInterval: 10.0);
//
//        request.httpMethod = "POST";
//
//        request.setValue("application/x-www-form-urlencoded; charset=utf-8",forHTTPHeaderField:"Content-Type")
//
//        request.setValue(base.auth(),forHTTPHeaderField:"Authorization")
//        request.httpBody = queryString.data(using: String.Encoding.utf8)
//
//        let config:URLSessionConfiguration = URLSessionConfiguration.default;
//            let session = Foundation.URLSession(configuration: config, delegate: nil, delegateQueue: nil)
//
//            let semaphore = DispatchSemaphore(value: 0)
//
//            session.dataTask(with: request) {[weak self] (data, response, error) in
//
//            if error != nil {
//                self?.token = nil;
//            }else{
//                let resp = response as! HTTPURLResponse;
//                if resp.statusCode == 200 {
//                    let str = NSString(data: data!, encoding: String.Encoding.utf8.rawValue) as String? ?? ""
//                    self?.token = OAuth2Token.loadToken(str,type: type);
//                }else{
//                    self?.token = nil;
//                }
//            }
//
//            semaphore.signal();
//
//        }.resume();
//
//        return semaphore;
    }

    private void getToken(OAuth2GrantType type,int timeout) throws IOException {
        String queryString = null;
        if(grantType == OAuth2GrantType.Password){
            queryString = "grant_type="+grantType+"&username="+username+"&password="+password+"&scope="+scope;
        }else{
            queryString = "grant_type=client_credentials";
        }
        tokenImpl(type,
                queryString,
                timeout
        );
//        _ = semaphore.wait(timeout: DispatchTime.init(uptimeNanoseconds: DispatchTime.now().uptimeNanoseconds + 5 * 1000 * 1000 * 1000))
    }

    public BaseAuthentication getBase() {
        return base;
    }

    public void setBase(BaseAuthentication base) {
        this.base = base;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
        base.setUsername(clientId);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
        base.setPassword(secret);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OAuth2GrantType getGrantType() {
        return grantType;
    }

    public void setGrantType(OAuth2GrantType grantType) {
        if(grantType == this.grantType){
            return;
        }
        if(grantType == null){
            grantType = OAuth2GrantType.Password;
        }
        this.grantType = grantType;
        setToken(null);
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public OAuth2TokenStore getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(OAuth2TokenStore tokenStore) {
        this.tokenStore = tokenStore;
        token = tokenStore.load();

        if(token.getType() == null){
            grantType = token.getType();
        }else{
            grantType = OAuth2GrantType.Password;
        }
    }

    private void setToken(OAuth2Token token) {
        this.token = token;
        if(tokenStore != null){
            tokenStore.store(token);
        }
    }
}
