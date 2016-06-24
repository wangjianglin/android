package lin.client.httpdns;

/**
 * Created by lin on 5/5/16.
 */
public class HostObject {
    private String hostName;
    private String ip;
    private long ttl;
    private long queryTime;

    public boolean isExpired() {
        return getQueryTime() + ttl < System.currentTimeMillis() / 1000;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
}
