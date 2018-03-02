package lin.comm.http.auth;

import android.media.session.MediaSession;

import java.util.Date;

import lin.util.JsonUtil;

/**
 * Created by lin on 14/01/2018.
 */

public class OAuth2Token {

    public static class ResultData{
        private String scope;
        private String access_token;
        private String token_type;
        private int expires_in;
        private String jti;
        private String openId;
        private String refresh_token;
        private String type;

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getJti() {
            return jti;
        }

        public void setJti(String jti) {
            this.jti = jti;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    private String accessToke;
    private String tokenType;
    private String refreshToken;
    private String scope;
    private long expiresIn;
    private String jti;
    private String openId;

    private OAuth2GrantType type;

    private String str;

    public String toString(){
        return str;
    }

    public static OAuth2Token loadToken(String str) {
        return loadToken(str, null);
    }
//
    static OAuth2Token loadToken(String str,OAuth2GrantType type){
//        let json = Json.init(string: str)
        ResultData result = null;
        try{
            result = JsonUtil.deserialize(str,ResultData.class);
        }catch (Throwable e){
            e.printStackTrace();
            return null;
        }

        if(result == null){
            return null;
        }

        OAuth2Token token = new OAuth2Token();
        token.setAccessToke(result.access_token);
        token.setRefreshToken(result.refresh_token);
        token.setTokenType(result.token_type);
        token.setJti(result.jti);
        token.setScope(result.scope);
        token.setOpenId(result.openId);

        int exp = result.expires_in;
        if(exp < 300){
            exp = exp / 2;
        }else{
            exp -= 300;
        }
        if(exp < 60){
            exp = 60;
        }

        exp += new Date().getTime() / 1000;

        token.setExpiresIn(exp);

        if(type != null){
            result.type = type.toString();
            token.setType(type);
            token.str = JsonUtil.serialize(result);
        }else{
            if("password".equals(result.type)){
                token.setType(OAuth2GrantType.Password);
            }else{
                token.setType(OAuth2GrantType.Client);
            }
            token.str = str;
        }

        return token;
    }

    public String getAccessToke() {
        return accessToke;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public String getJti() {
        return jti;
    }

    public String getOpenId() {
        return openId;
    }

    public OAuth2GrantType getType() {
        return type;
    }

    private void setType(OAuth2GrantType type) {
        this.type = type;
    }

    private void setAccessToke(String accessToke) {
        this.accessToke = accessToke;
    }

    private void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    private void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    private void setScope(String scope) {
        this.scope = scope;
    }

    private void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    private void setJti(String jti) {
        this.jti = jti;
    }

    private void setOpenId(String openId) {
        this.openId = openId;
    }
}
