package io.cess.comm.http;

import java.net.CookieManager;
import java.net.CookieStore;

/**
 * @author lin
 * @date 1/29/16.
 */
class SessionInfo {

//    volatile String cookie;
    volatile CookieStore mCookieStore = new CookieManager().getCookieStore();
}
