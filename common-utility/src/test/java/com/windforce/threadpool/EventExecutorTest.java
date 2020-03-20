package com.windforce.threadpool;

import com.windforce.common.threadpool.AbstractDispatcherHashCodeRunable;
import com.windforce.common.threadpool.IdentityEventExecutorGroup;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EventExecutorTest {

    public static void main(String[] args) {
        IdentityEventExecutorGroup.init(3);
        ScheduledFuture<?> future = IdentityEventExecutorGroup
                .addScheduleAtFixedRate(new AbstractDispatcherHashCodeRunable() {

                    @Override
                    public long timeoutNanoTime() {
                        return 0;
                    }

                    @Override
                    public String name() {
                        return "EventExecutorTest";
                    }

                    @Override
                    public int getDispatcherHashCode() {
                        return 1;
                    }

                    @Override
                    protected void doRun() {
                        System.out.println(new Date());

                    }
                }, 1, 1, TimeUnit.SECONDS);

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(5000);
                if (i == 2) {
                    future.cancel(false);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            IdentityEventExecutorGroup.printRunableStatistics();
        }
    }
}
