package lock;

import java.util.concurrent.TimeUnit;

/**
 * Created by sonnyching on 2017/12/4.
 */
public class TestLock {

    public static void main(String[] args) {
        testShareLock();
    }

    public static void testShareLock(){
        final ShareLock lock = new ShareLock();
        for (int i = 0; i < 10; i++) {
            new Thread(){
                @Override
                public void run() {
                    while(true){
                        lock.lock();
                        try {
                            TimeUnit.SECONDS.sleep(2);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        finally {
                            lock.unlock();
                        }
                    }
                }
            }.start();
        }
    }

    public static void testExclusiveLock(){
        final ExclusiveLock lock = new ExclusiveLock();
        class Work extends Thread{
            @Override
            public void run() {
                while (true){
                    try {
                        lock.lock();
                        TimeUnit.SECONDS.sleep(3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        lock.unlock();
                    }
                }
            }
        }

        System.out.println("start--------");


        for (int i = 0; i < 10; i++) {
            Work work = new Work();
            work.start();
        }

        System.out.println("end-------");


    }

}
