package com.wytiger.aidlwrapper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wytiger.sdk.IHelloServer;
import com.wytiger.sdk.client.HelloServerManager;
import com.wytiger.sdk.client.HelloServerManager2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HelloServerManager.getInstance().init(MainActivity.this);
        HelloServerManager2.getInstance().init(MainActivity.this);

        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCallServer();
            }
        });
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                testCallServer2();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void testCallServer2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    IHelloServer helloServer = HelloServerManager2.getInstance().getHelloServer();
                    helloServer.sayHello();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void testCallServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    IHelloServer helloServer = HelloServerManager.getInstance().getHelloServer();
                    helloServer.sayHello();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
