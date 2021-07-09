package com.moonspirit.concurrent.jcart;

import java.util.concurrent.TimeUnit;

/**
 * 线程状态切换条件：
 * 〇 -> NEW，创建线程实例，尚未启动线程
 * NEW -> RUNNABLE，Thread.start() 启动线程
 * RUNNABLE -> TERMINATED，线程执行完毕并退出
 *
 * 进入等待状态的线程需要依靠其他线程的通知才能返回运行状态
 * RUNNABLE -> WAITING，Object.wait()/Ojbect.join()/LockSupport.park()
 * WAITING -> RUNNABLE，Object.notify()/Object.notifyAll()/LockSupport.unpark(Thread)
 *
 * 进入超时等待状态的线程当到达超时时间或者其他线程的通知时返回运行状态
 * RUNNABLE -> TIMED_WAITING，Object.wait()/Ojbect.join()/LockSupport.park()
 * TIMED_WAITING -> RUNNABLE，Object.notify()/Object.notifyAll()/LockSupport.unpark(Thread)
 *
 * RUNNALBE -> BLOCKED，等待获取锁，进入阻塞状态
 * BLOCKED -> RUNNABLE，成功获取锁，返回运行状态
 */
public class ThreadState {
    public static void main(String[] args) {
        // Thread.sleep(10)进入超时等待状态，并且不释放锁
        new Thread(new TimeWaiting(),"TimeWaitingThread").start();

        // Ojbect.wait()进入等待状态，同时释放锁
        new Thread(new Waiting(),"WaitingThread").start();

        // Thread.sleep(10)进入超时等待状态，并且不释放锁
        new Thread(new Blocked(),"BlockedThread-1").start();

        // 由于BlockedThread-1一直持有类锁，BlockedThread-2处于阻塞状态
        new Thread(new Blocked(),"BlockedThread-2").start();
    }

    static class TimeWaiting implements Runnable{
        @Override
        public void run() {
            while(true) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Waiting implements Runnable {
        @Override
        public void run() {
            while(true) {
                synchronized(Waiting.class) {
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class Blocked implements Runnable{
        @Override
        public void run() {
            synchronized(Blocked.class) {
                while (true){
                    try {
                        TimeUnit.SECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
