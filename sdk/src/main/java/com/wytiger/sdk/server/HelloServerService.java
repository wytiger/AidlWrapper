package com.wytiger.sdk.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HelloServerService extends Service {
    private HelloServer helloServer;

    public HelloServerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        helloServer = new HelloServer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return helloServer;
    }
}
