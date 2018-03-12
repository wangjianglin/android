package io.cess.comm.http;

/**
 * @author lin
 * @date 18/05/2017.
 */

public class HttpCommParams {

    private Integer timeout;
    private Boolean mainThread;
    private Boolean debug;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean isMainThread() {
        return mainThread;
    }

    public void setMainThread(Boolean mainThread) {
        this.mainThread = mainThread;
    }

    public Boolean isDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }
}
