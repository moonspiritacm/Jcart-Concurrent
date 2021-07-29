package com.moonspirit.concurrent.jcart.ch04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHttpServer {
    // 线程池
    static ThreadPool<HttpRequestHandler> threadPool = new DefaultThreadPool<>(1);

    // 服务器根目录
    static String basePath;

    static ServerSocket serverSocket;

    // 服务器监听端口
    static int port = 8080;

    public static void setPort(int port) {
        if (port >= 0 && port <= 65535) {
            SimpleHttpServer.port = port;
        }
    }

    public static void start() throws IOException {
        serverSocket = new ServerSocket(port);
        Socket socket = null;
        while ((socket = serverSocket.accept()) != null) {
            threadPool.execute(new HttpRequestHandler(socket));
        }
        serverSocket.close();
    }

    static class HttpRequestHandler implements Runnable {
        private Socket socket;

        public HttpRequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
