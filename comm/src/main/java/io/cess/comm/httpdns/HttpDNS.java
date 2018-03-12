/**
 * Http DNS 主要解决 DNS 劫持问题
 */
package io.cess.comm.httpdns;

import java.util.List;

/**
 * @author lin
 * @date 4/25/16.
 */
public interface HttpDNS {

    /**
     * 根据 host 查询 ip
     * @param host
     * @return
     */
    String getIpByHost(String host);

    /**
     * 是否允许使用过期的IP，默认允许
     * @param flag
     */
    void setExpiredIpAvailable(boolean flag);

    boolean isExpiredIpAvailable();

    /**
     * dns 阶级策略
     * @param filter
     */
    void setDegradationFilter(DegradationFilter filter);

    /**
     * 预加载
     * @param hosts
     */
    void setPreResolveHosts(String ... hosts);

    /**
     * 设置ip选取策略
     * @param sessionMode
     */
    void setSessionMode(SessionMode sessionMode);

    interface DegradationFilter {
        boolean shouldDegradeHttpDNS(String hostName);
    }

    /**
     * 当同一个 host 对应多个ip时，ip选取策略
     * Sticky：表示app在整个
     */
    enum SessionMode{
        Sticky,Random
    }
}
