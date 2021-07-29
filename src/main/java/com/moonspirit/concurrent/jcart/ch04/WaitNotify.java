package com.moonspirit.concurrent.jcart.ch04;

public class WaitNotify {
    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
    }

    static class Wait implements Runnable {
        @Override
        public void run() {
            // 加锁，获得lock的Monitor
            synchronized(lock) {
                while(flag)
            }
        }
    }
}
