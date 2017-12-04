package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by sonnyching on 2017/12/4.
 */
public class ExclusiveLock implements Lock {

    final static int maxThreadAccess = 1;
    private final Sync lock = new Sync();

    private static class Sync extends AbstractQueuedSynchronizer{
        protected Sync() {
            super();
        }

        @Override
        protected boolean tryAcquire(int acqs) {

            int current = getState();
            int total = current + acqs;
            if(total > maxThreadAccess){
                return false;
            }
            if (compareAndSetState(current,total)){
                System.out.println("获得锁--"+Thread.currentThread().getId());
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }

            return false;
        }

        @Override
        protected boolean tryRelease(int release) {
            int current = getState();
            int after = current - release;
            if(current <= 0 || after < 0){
                return false;
            }
            if (compareAndSetState(current,after)){
                System.out.println("---释放锁--"+Thread.currentThread().getId());

                setExclusiveOwnerThread(null);
                return true;
            }

            return false;
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }
    }


    public void lock() {
        lock.acquire(1);
    }

    public void unlock() {
        lock.release(1);
    }


    public void lockInterruptibly() throws InterruptedException {
        lock.acquireInterruptibly(1);
    }

    public boolean tryLock() {
        return lock.tryAcquire(1);
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return tryLock(time,unit);
    }

    public Condition newCondition() {
        return null;
    }
}
