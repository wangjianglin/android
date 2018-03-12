package io.cess.comm.http;

/**
 * @author lin
 * @date 9/24/15.
 */
public interface ProgressResultListener<T> extends ResultListener<T>{
    void progress(long progress,long total);
}
