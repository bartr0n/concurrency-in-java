package fr.xebia.java.concurrency;

import java.util.concurrent.ThreadFactory;

public class CustomThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {

        final Thread thread = new Thread(r, "custom-thread");
        thread.setPriority(Thread.MIN_PRIORITY);

        return thread;
    }
}
