package com.wytiger.aidlwrapper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.wytiger.sdk.IHelloServer;
import com.wytiger.sdk.client.HelloServerManager;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HelloServerManager.getInstance().init(MainActivity.this);

        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCallServer();
            }
        });
    }

    private void testCallServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    IHelloServer helloServer = HelloServerManager.getInstance().getHelloServer(MainActivity.this);
                    helloServer.sayHello();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
