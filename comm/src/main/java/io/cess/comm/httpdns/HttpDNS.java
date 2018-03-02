package io.cess.comm.httpdns;

import java.util.List;

/**
 * Created by lin on 4/25/16.
 */
public interface HttpDNS {
    String getIpByHost(String host);

    // 是否允许过期的IP返回，默认允许
    void setExpiredIpAvailable(boolean flag);

    boolean isExpiredIpAvailable();

    void setDegradationFilter(DegradationFilter filter);

    void setPreResolveHosts(List<String> hosts);

    interface DegradationFilter {
        boolean shouldDegradeHttpDNS(String hostName);
    }

    enum SessionMode{
        Sticky,Random
    }
}
