package lock;

import java.util.concurrent.TimeUnit;

/**
 * Created by sonnyching on 2017/12/4.
 */
public class TestLock {

    public static void main(String[] args) {

        String newString = new String("323");
        synchronized (newString){
            try {
                newString.notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        testJoin();
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


    private static class stopThread extends  Thread{
        private boolean isRunning = true;

        @Override
        public void run() {
            while(isRunning && !Thread.currentThread().isInterrupted()){
                System.out.println("i am running~~~");
            }
        }

        public void stopThread(){
            this.isRunning = false;
        }

    }


    private static void testJoin(){
        Thread b = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    System.out.println("B线程结束啦");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        b.start();

        try {
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程结束啦");

    }

}
