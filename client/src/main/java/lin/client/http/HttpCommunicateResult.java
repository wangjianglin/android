package lin.client.http;

import lin.util.thread.AutoResetEvent;


/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:02:52
 *
 */
public class HttpCommunicateResult {
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
    private  Object _obj;
    public void setResult(boolean result,Object obj){
//    	this.lock.lock();
    	this._result = result;
    	this._obj = obj;
//    	this.condition.signalAll();
//    	this.lock.unlock();
    }
    public Object getResult(){
    	this.waitForEnd();
    	return _obj;
    }
    public boolean isSuccess()
    {
    	this.waitForEnd();
    	return this._result;
    }
}
