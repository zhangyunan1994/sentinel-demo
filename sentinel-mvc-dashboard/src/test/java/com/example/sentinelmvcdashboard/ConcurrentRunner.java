package com.example.sentinelmvcdashboard;

import com.alibaba.csp.sentinel.util.TimeUtil;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ConcurrentRunner {

    private final int threadCount;
    private int seconds;
    private Supplier supplier;

    public ConcurrentRunner(int threadCount, int seconds, Supplier supplier) {
        this.threadCount = threadCount;
        this.seconds = seconds;
        this.supplier = supplier;
    }

    private final AtomicInteger pass = new AtomicInteger();
    private final AtomicInteger block = new AtomicInteger();
    private final AtomicInteger total = new AtomicInteger();

    private volatile boolean stop = false;

    public void simulateTraffic() {
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new RunTask());
            t.setName("simulate-traffic-Task");
            t.start();
        }
    }

    public void tick() {
        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer-task");
        timer.run();
    }

    final class RunTask implements Runnable {
        @Override
        public void run() {
            while (!stop) {

                try {
                    supplier.get();
                    // token acquired, means pass
                    pass.addAndGet(1);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    block.incrementAndGet();

                    // biz exception
                } finally {
                    total.incrementAndGet();
                }

                Random random2 = new Random();
                try {
                    TimeUnit.MILLISECONDS.sleep(random2.nextInt(50));
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }

    final class TimerTask implements Runnable {
        @Override
        public void run() {
            long start = System.currentTimeMillis();
            System.out.println("begin to statistic!!!");

            long oldTotal = 0;
            long oldPass = 0;
            long oldBlock = 0;
            while (!stop) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                }
                long globalTotal = total.get();
                long oneSecondTotal = globalTotal - oldTotal;
                oldTotal = globalTotal;

                long globalPass = pass.get();
                long oneSecondPass = globalPass - oldPass;
                oldPass = globalPass;

                long globalBlock = block.get();
                long oneSecondBlock = globalBlock - oldBlock;
                oldBlock = globalBlock;

                System.out.println(seconds + " send qps is: " + oneSecondTotal);
                System.out.println(TimeUtil.currentTimeMillis() + ", total:" + oneSecondTotal
                        + ", pass:" + oneSecondPass
                        + ", block:" + oneSecondBlock);

                if (seconds-- <= 0) {
                    stop = true;
                }
            }

            long cost = System.currentTimeMillis() - start;
            System.out.println("time cost: " + cost + " ms");
            System.out.println("total:" + total.get() + ", pass:" + pass.get()
                    + ", block:" + block.get());
            System.exit(0);
        }
    }
}
