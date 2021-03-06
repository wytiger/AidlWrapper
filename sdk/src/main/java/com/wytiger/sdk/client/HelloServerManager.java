package com.wytiger.sdk.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.wytiger.sdk.IHelloServer;
import com.wytiger.sdk.server.HelloServerService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * desc:
 *
 * @author wuyong_cd
 */
public class HelloServerManager {
    private static HelloServerManager INSTANCE = new HelloServerManager();

    private Context context;
    private Object LOCK = new Object();
    ExecutorService executorService;
    FetchHelloServerCallable callable;
    IHelloServer helloServer;

    private HelloServerManager() {
        executorService = Executors.newSingleThreadExecutor();
        callable = new FetchHelloServerCallable();
    }

    public static HelloServerManager getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化，绑定服务
     *
     * @param context 上下文
     */
    public void init(Context context) {
        this.context = context.getApplicationContext();
        bindService(context);
    }

    /**
     * 获取服务，可能是耗时任务
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public IHelloServer getHelloServer() throws ExecutionException, InterruptedException {
        this.context = context.getApplicationContext();

        Future<IHelloServer> helloServerFuture = executorService.submit(callable);
//        executorService.shutdown();

        return helloServerFuture.get();//阻塞等待结果
    }

    public class FetchHelloServerCallable implements Callable<IHelloServer> {
        @Override
        public IHelloServer call() throws Exception {
            Log.d("TAG", "call");
            if (helloServer == null) {
                synchronized (LOCK) {
                    //绑定服务
                    bindService(context);
                    //挂起,等待唤醒
                    LOCK.wait();
                }
            }

            return helloServer;
        }
    }

    private synchronized void bindService(Context context) {
        Intent intent = new Intent(context, HelloServerService.class);
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                synchronized (LOCK) {
                    helloServer = IHelloServer.Stub.asInterface(service);
                    LOCK.notifyAll();//唤醒
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                synchronized (LOCK) {
                    helloServer = null;
                    LOCK.notifyAll();//唤醒
                }
            }
        }, Context.BIND_AUTO_CREATE);
    }
}
