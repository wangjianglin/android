package lin.client.http1;


/**
 * Created by lin on 9/24/15.
 */
public interface ProgressResultListener extends ResultListener{
    void progress(long progress, long total);
}
