package io.cess.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author lin
 * @date 14/01/2018.
 */

public class ThreadUtil {

    private static Handler mMainHandler = new Handler(Looper.getMainLooper());
    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2,
            20,
            20,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(3000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static boolean mainThread(Runnable runnable){
        return mMainHandler.post(runnable);
    }

    public static boolean postMainThreadAtTime(Runnable r, long uptimeMillis){
        return mMainHandler.postAtTime(r,uptimeMillis);
    }

    public final boolean postMainThreadAtTime(Runnable r, Object token, long uptimeMillis){
        return mMainHandler.postAtTime(r,token,uptimeMillis);
    }

    public final boolean postMainThreadDelayed(Runnable r, long delayMillis){
        return mMainHandler.postDelayed(r,delayMillis);
    }

    public static void newThread(Runnable runnable){
        new Thread(runnable).start();
    }

    public static void asynQueue(Runnable runnable){
        poolExecutor.execute(runnable);
    }
}
