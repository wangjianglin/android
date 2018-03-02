package io.cess.comm.http;

import java.net.CookieManager;
import java.net.CookieStore;

/**
 * Created by lin on 1/29/16.
 */
class SessionInfo {

//    volatile String cookie;
    volatile CookieStore mCookieStore = new CookieManager().getCookieStore();
}
