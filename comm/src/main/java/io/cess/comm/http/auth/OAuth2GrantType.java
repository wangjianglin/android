package io.cess.comm.http.auth;

/**
 * Created by lin on 14/01/2018.
 */

public enum OAuth2GrantType {

    Password("password"),Client("client_credentials");

    private String str;
    OAuth2GrantType(String str){
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
