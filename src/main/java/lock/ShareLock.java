package lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by sonnyching on 2017/12/4.
 */
public class ShareLock implements Lock{

    private static final int maxThread = 2;
    private final static  ShareSync lock = new ShareSync(maxThread);

    private static final class ShareSync extends AbstractQueuedSynchronizer{

        ShareSync(int maxThread) {
            super();
            if(maxThread <= 0){
                throw new IllegalArgumentException("maxThread must lager than zero!");
            }
            setState(maxThread);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            int current = getState();
            int after = current - arg;
            //after<0表示获取锁失败，compareAndSetState 为true表示获取成功，并且after>0
            if (after < 0) {
                System.out.println("获取共享锁失败1---" + Thread.currentThread().getId() + "," + getState());
                return after;
            } else {
                boolean result = compareAndSetState(current, after);
                if (result) {
                    System.out.println("---获取共享锁成功---" + Thread.currentThread().getId() + "," + after);
                    return after;
                } else {
                    System.out.println("获取共享锁失败2---" + Thread.currentThread().getId() + "," + getState());
                }
            }
            return -99;

        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            int current = getState();
            int after = current + arg;
            //防止多次调release造成state>maxThread的问题
            if(after > maxThread){
                System.out.println("0000000000释放共享锁失败---"+Thread.currentThread().getId());

                return false;
            }
            if(compareAndSetState(current,after)){
                System.out.println("~~~~~~~~~~~释放共享锁成功~~~~~~~"+Thread.currentThread().getId()+"，"+after);

                return true;
            }
            return false;
//            for (;;) {
//
//            }
        }

    }

    @Override
    public void lock() {
        lock.acquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return lock.tryAcquireShared(1)>0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        lock.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
