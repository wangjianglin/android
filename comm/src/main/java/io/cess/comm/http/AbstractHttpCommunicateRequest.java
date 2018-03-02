//package io.cess.comm.http;
//
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by lin on 24/06/2017.
// */
//
//public abstract class AbstractHttpCommunicateRequest  implements HttpCommunicateRequest {
//
//    protected HttpPackage mPack;
//    protected ResultListener mListener;
//    protected HttpCommunicateImpl mImpl;
//    protected HttpCommunicate.Params mParams;
//
//    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 50, 10,
//            TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(3000),
//            new ThreadPoolExecutor.CallerRunsPolicy());
//
//    @Override
//    public void setPackage(HttpPackage pack) {
//        this.mPack = pack;
//    }
//
//    @Override
//    public void setImpl(HttpCommunicateImpl impl) {
//        this.mImpl = impl;
//    }
//
//    @Override
//    public void setListener(ResultListener listener) {
//        this.mListener = listener;
//    }
//
//    public void request(){
//
//        Runnable task = getTask();
//        executor.execute(task);
//    }
//
//    protected abstract Runnable getTask();
//
//    @Override
//    public void setParams(HttpCommunicate.Params params) {
//        this.mParams = params;
//    }
//
//    @Override
//    public void abort() {
//
//    }
//}