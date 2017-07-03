package lin.comm.http;

/**
 * Created by lin on 9/24/15.
 */
public interface ProgressResultListener<T> extends ResultListener<T>{
    void progress(long progress,long total);
}
