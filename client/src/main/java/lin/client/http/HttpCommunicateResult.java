package lin.client.http;

import java.util.List;

import lin.util.thread.AutoResetEvent;


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
    Aboutable request ;
	public HttpCommunicateResult()
    {
		//this.lock = lock;
        //this.are = are;
        //this.abort = abort;
    }

    private Boolean _result = null;
    private List<Error> warning;
    private Error error;

	private AutoResetEvent set = new AutoResetEvent(false);

    AutoResetEvent getAutoResetEvent(){
        return set;
    }

    public void abort()
    {
    	request.abort();	
    }

    long threadId = -1;
    public HttpCommunicateResult waitForEnd()
    {
    	threadId = Thread.currentThread().getId();
    	set.waitOne();
    	return this;
    }
    private  T _obj;
    public void setResult(boolean result, T obj, List<Error> warning,Error error){
//    	this.lock.lock();
    	this._result = result;
    	this._obj = obj;
        this.warning = warning;
        this.error = error;
//    	this.condition.signalAll();
//    	this.lock.unlock();
    }
    public T getResult(){
    	this.waitForEnd();
    	return _obj;
    }
    public List<Error> getWarning(){
        return warning;
    }
    public Error getError(){
        return error;
    }
    public boolean isSuccess()
    {
    	this.waitForEnd();
    	return this._result;
    }
}
