package io.cess.comm.http;

import java.util.List;

import io.cess.util.thread.AutoResetEvent;


/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:02:52
 *
 */
public class HttpCommunicateResult<T> {
	public static final long ABORT_CODE = 0x2001000;
    //private AutoResetEvent are;
    //private System.Action abort;

    //HttpCommunicateResult(AutoResetEvent are, System.Action abort)
//	ReentrantLock lock = new ReentrantLock();
//	Condition condition = lock.newCondition();
    Aboutable mRequest ;
	public HttpCommunicateResult()
    {
		//this.lock = lock;
        //this.are = are;
        //this.abort = abort;
    }

    private Boolean mSuccess = null;
    private List<Error> mWarning;
    private Error mError;

	private AutoResetEvent mSet = new AutoResetEvent(false);

    AutoResetEvent getAutoResetEvent(){
        return mSet;
    }

    public void abort()
    {
    	mRequest.abort();
    }

    long mThreadId = -1;
    public HttpCommunicateResult waitForEnd()
    {
        mThreadId = Thread.currentThread().getId();
        mSet.waitOne();
    	return this;
    }
    private  T mObj;
    public void setResult(boolean result, T obj, List<Error> warning,Error error){
//    	this.lock.lock();
    	this.mSuccess = result;
    	this.mObj = obj;
        this.mWarning = warning;
        this.mError = error;
//    	this.condition.signalAll();
//    	this.lock.unlock();
    }
    public T getResult(){
    	this.waitForEnd();
    	return mObj;
    }
    public List<Error> getWarning(){
        return mWarning;
    }
    public Error getError(){
        return mError;
    }
    public boolean isSuccess()
    {
    	this.waitForEnd();
    	return this.mSuccess;
    }
}
