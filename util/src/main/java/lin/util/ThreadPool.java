package lin.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程池
 */
public class ThreadPool {

    private volatile int coreSize;
    private volatile int maxSize;
    private volatile int queueSize;

    public ThreadPool(int coreSize,int maxSize,int queueSize){
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.queueSize = queueSize;

        this.init();
    }

    private void init(){
        queue = new LinkedList<Runnable>();

        threads = new ExecueRunnable[maxSize>coreSize?maxSize:coreSize];

        Thread thread = null;
        for(int n=0;n<coreSize;n++){
            threads[n] = new ExecueRunnable();
            threads[n].isCoreThread = true;
            thread = new Thread(threads[n]);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private volatile LinkedList<Runnable> queue;
    private volatile ExecueRunnable[] threads;

    private volatile ReentrantLock lock = new ReentrantLock();
    private volatile Condition condition = lock.newCondition();
    public void execute(Runnable runnable){
        if(runnable == null){
            return;
        }
//        synchronized (queue){
        lock.lock();
        queue.add(runnable);

        condition.signalAll();

        lock.unlock();
//        }
    }

    private class ExecueRunnable implements Runnable{

        private boolean isCoreThread = false;
        private boolean isStop = false;
        @Override
        public void run() {
            boolean isBreak = false;
            while (true){
                try {
                    isBreak = runImpl();
                }catch (Throwable e){
                }

                if(!isCoreThread && isBreak){
                //if(isBreak){
                    break;
                }
                if(isBreak) {
                    lock.lock();
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        break;
                    }finally {
                        lock.unlock();
                    }

//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//
//                    }
                }
            }
            isStop = true;
        }

        private boolean runImpl(){
            Runnable runnable = null;
            synchronized (queue){
                runnable = queue.poll();
            }
            if (runnable == null){
                return true;
            }
            runnable.run();
            return false;
        }
    }
}
