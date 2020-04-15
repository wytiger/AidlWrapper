package com.wytiger.sdk.server;

import android.os.RemoteException;
import android.util.Log;

import com.wytiger.sdk.IHelloServer;

/**
 * desc:
 *
 * @author wuyong_cd
 */
public class HelloServer extends IHelloServer.Stub {
    @Override
    public void sayHello() throws RemoteException {
        Log.d("TAG","HelloServer: hello");
    }
}
