package lin.client.http1;

import java.util.List;


/**
 * 
 * @author 王江林
 * @date 2013-7-16 上午11:28:01
 *
 */
public interface ResultListener {
	 void result(Object obj, List<Error> warning);
	 void fault(Error error);
}


