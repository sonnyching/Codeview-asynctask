package guava;

import com.google.common.util.concurrent.*;
import com.sun.istack.internal.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * it's just guava test
 */
public class GuavaTest {

    public static void main(String[] args) {

        System.out.println("main thread start~");

        final ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        final ListenableFuture<String> future = service.submit(new Callable<String>() {
            public String call() throws Exception {
                try{
                   Thread.sleep(5000);
                    System.out.println("----i am running in the background!"+"-----"+Thread.currentThread().getId());
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                return "---------over!";
            }
        });

        Futures.addCallback(future, new FutureCallback<String>() {
            public void onSuccess(String s) {
                System.out.println("-----------call back--success--"+s+"-----"+Thread.currentThread().getName());

            }

            public void onFailure(Throwable throwable) {
                System.out.println("-----------call back--failed--"+throwable.getMessage());
            }
        });


        System.out.println("main thread continue run~"+"-----"+Thread.currentThread().getName());

//        service.shutdown();
        service.shutdown();
        try {
            if(service.awaitTermination(5, TimeUnit.SECONDS)){
                System.out.println("pool closed~");
            }else{
                System.out.println("pool didn't closed~");
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }

}
