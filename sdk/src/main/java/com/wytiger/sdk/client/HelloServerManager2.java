package com.wytiger.sdk.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

import com.wytiger.sdk.IHelloServer;
import com.wytiger.sdk.server.HelloServerService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/**
 * desc:
 *
 * @author wuyong_cd
 * @date 2020/8/27 0027
 */
public class HelloServerManager2 {
    private static HelloServerManager2 INSTANCE = new HelloServerManager2();

    private Context context;
    private IHelloServer helloServer;

    private HelloServerManager2() {
    }

    public static HelloServerManager2 getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化，绑定服务
     *
     * @param context 上下文
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void init(Context context) {
        this.context = context.getApplicationContext();
        bindService(context, null);
    }

    /**
     * 获取服务，可能是耗时任务
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public IHelloServer getHelloServer() throws ExecutionException, InterruptedException {
        this.context = context.getApplicationContext();

        CompletableFuture<IHelloServer> helloServerFuture = new CompletableFuture<>();
        bindService(context, helloServerFuture);

        return helloServerFuture.get();//阻塞等待结果
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private synchronized void bindService(Context context, final CompletableFuture<IHelloServer> helloServerFuture) {
        Intent intent = new Intent(context, HelloServerService.class);
        context.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                helloServer = IHelloServer.Stub.asInterface(service);
                //完成通知
                if (helloServerFuture != null) {
                    helloServerFuture.complete(helloServer);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                helloServer = null;
                //完成通知
                if (helloServerFuture != null) {
                    helloServerFuture.complete(null);
                }
            }
        }, Context.BIND_AUTO_CREATE);
    }
}
