package io.cess.comm.http.auth;

import io.cess.util.Base64;

/**
 * Created by lin on 14/01/2018.
 */

public class BaseAuthentication implements Authentication {

    private String username;
    private String password;

    public BaseAuthentication(){}

    public BaseAuthentication(String username,String password){
        this.username = username;
        this.password = password;
    }

    public String auth() {

        String data = username + ":" + password;
        return "Basic " + Base64.encode(data);
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
}
