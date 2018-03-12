package io.cess.comm.http.okhttp3;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import io.cess.comm.httpdns.HttpDNS;
import okhttp3.Dns;

/**
 * @author lin
 * @date 07/03/2018.
 */

class OkHttp3Dns implements Dns {

    private HttpDNS mHttpDns;

    public OkHttp3Dns(HttpDNS httpDns) {
        this.mHttpDns = httpDns;
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if(mHttpDns == null){
            return Dns.SYSTEM.lookup(hostname);
        }
        String destIp = mHttpDns.getIpByHost(hostname);
        if(destIp != null && !"".equals(destIp.trim())){
            List<InetAddress> addresses = Arrays.asList(InetAddress.getAllByName(destIp));
            if(addresses != null && !addresses.isEmpty()){
                return addresses;
            }
        }
        return Dns.SYSTEM.lookup(hostname);
    }
}
