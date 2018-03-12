package io.cess.comm.http.auth;

/**
 * @author lin
 * @date 14/01/2018.
 */

public interface OAuth2TokenStore {
    void store(OAuth2Token token);
    OAuth2Token load();
}
