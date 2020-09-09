package Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    public static ExecutorService fixedThreadPool;

    public ThreadPoolManager() {
    }
    public static ExecutorService getFixedThreadPool(){
        if(fixedThreadPool==null){
            fixedThreadPool= Executors.newFixedThreadPool(4);
        }
        return fixedThreadPool;
    }
}
