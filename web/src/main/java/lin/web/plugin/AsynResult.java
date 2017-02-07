package lin.web.plugin;

import lin.util.thread.AutoResetEvent;

/**
 * 
 * @author lin
 * @date Jun 4, 2015 4:04:04 PM
 *
 */
public class AsynResult {

	private AutoResetEvent set = new AutoResetEvent(false);
	private Object result;
	public void setResult(Object result){
		this.result = result;
		set.set();
	}
	
	public Object getResult(){
		set.waitOne();
		return this.result;
	}
}
