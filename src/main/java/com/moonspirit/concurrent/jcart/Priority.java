package com.moonspirit.concurrent.jcart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 程序的正确性不能依赖线程的优先级，操作系统可能无视Java线程优先级设定。
 * 经测试，Win10，jdk1.8，线程优先级设定有效。
 */
public class Priority {
    private static volatile boolean notStart = true;
    private static volatile boolean notEnd = true;

    public static void main(String[] args) throws InterruptedException {
        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int priority = i < 5 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY;
            Job job = new Job(priority);
            jobs.add(job);
            Thread thread = new Thread(job, "Thread-" + i);
            thread.setPriority(priority);
            thread.start();
        }

        notStart = false;
        TimeUnit.SECONDS.sleep(100);

        notEnd = false;
        for (Job job : jobs) {
            System.out.println("Priority:" + job.priority + ", Count:" + job.count);
        }
    }

    static class Job implements Runnable {
        private int priority;
        private long count;

        public Job(int priority) {
            this.priority = priority;
        }

        @Override
        public void run() {
            while (notStart) {
                Thread.yield();
            }
            while (notEnd) {
                Thread.yield();
                count++;
            }
        }
    }
}
