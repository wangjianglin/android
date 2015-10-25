package lin.util.thread;

import lin.util.Action;

/**
 * 
 * @author lin
 * @date Mar 12, 2015 1:16:55 AM
 *
 */
public class ActionExecute {

	private static void executeImpl(Action[] actions,int n)
    {
        try
        {
            actions[n].action();
        }
        finally
        {
            n++;
            if (n < actions.length)
            {
                executeImpl(actions, n);
            }
        }
    }

    /**
     * 依次执行actions中的Action，确保每个Action都会被执行
     * 
     * @param System
     */
    public static void execute(Action ...actions)
    {
        if (actions != null && actions.length > 0)
        {
            executeImpl(actions,0);
        }
    }
	
}
