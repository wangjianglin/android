package lin.util.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程同步操作
 * @author 王江林
 * @date 2013-7-24 下午9:37:05
 *
 */
public class AutoResetEvent {

	private volatile ReentrantLock lock = new ReentrantLock();
	private volatile Condition condition = lock.newCondition();
	private volatile boolean isSet = false;
	/**
	 * 
	 * @param type true,变量所在线程
	 */
	public AutoResetEvent(boolean isSet){
		this.isSet = isSet;
	}
	
	public AutoResetEvent(){
		this.isSet = false;
	}
	
	public void set(){
		lock.lock();
		isSet = true;
		condition.signalAll();
		lock.unlock();
	}
	
	public void waitOne(){
		lock.lock();
		while(!isSet){
			try {
				condition.await();
			} catch (InterruptedException e) {
				break;
			}
		}
		lock.unlock();
	}
	
	public void reset(){
		lock.lock();
		isSet = false;
		condition.signalAll();
		lock.unlock();
	}
}
