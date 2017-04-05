package lin.comm.httpdns;

import java.util.Arrays;

/**
 * Created by lin on 5/5/16.
 */
public class HostObject {
    private String hostName;
    private String[] ips;
    private long ttl;
    private long queryTime;//得到ip时间
//    private long expiredRequestTime;//过期请求时间
    private HttpDNS.SessionMode sessionMode = HttpDNS.SessionMode.Sticky;
    private int ipIndex = 0;

//    public boolean isRequest(){
//        return expiredRequestTime + ttl < System.currentTimeMillis() / 1000;
//    }

    public boolean isExpired() {
        return getQueryTime() + ttl < System.currentTimeMillis() / 1000;
    }

    void status(HostObject obj){
        if(sessionMode == HttpDNS.SessionMode.Random){
            return;
        }
        if(obj == null){
            return;
        }
        if(this.ips == null || this.ips.length <= 1){
            return;
        }

        if(obj.ips == null || obj.ips.length == 0){
            return;
        }
        String oip = obj.ips[obj.ipIndex];
        if(oip == null || "".equals(oip)){
            return;
        }
        for(int n=0;n<ips.length;n++){
            if(oip.equals(ips[n])){
                ipIndex = n;
                break;
            }
        }
    }

    public String getIp(){
        if(this.ips == null || this.ips.length == 0){
            return null;
        }
        if(this.ips.length == 1){
            return this.ips[0];
        }
        if(sessionMode == HttpDNS.SessionMode.Sticky){
            return ips[ipIndex];
        }
        return ips[(int)(Math.random()*ips.length) % ips.length];
    }

    public String[] getIps() {
        if(ips == null){
            return null;
        }
        return Arrays.copyOf(ips,ips.length);
    }

    public void setIps(String[] ips) {
        if(ips == null){
            this.ips = null;
            return;
        }
        this.ips = Arrays.copyOf(ips,ips.length);
        ipIndex = (int)(Math.random()*ips.length) % ips.length;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return hostName;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public long getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(long queryTime) {
        this.queryTime = queryTime;
    }

    public long getExpiredRequestTime() {
        return expiredRequestTime;
    }

    public void setExpiredRequestTime(long expiredRequestTime) {
        this.expiredRequestTime = expiredRequestTime;
    }

    public HttpDNS.SessionMode getSessionMode() {
        return sessionMode;
    }

    public void setSessionMode(HttpDNS.SessionMode sessionMode) {
        this.sessionMode = sessionMode;
    }
}
