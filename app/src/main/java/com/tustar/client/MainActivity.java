package com.tustar.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tustar.client.util.Logger;
import com.tustar.demo.service.IDemoAidlService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.bind_service)
    Button mBindService;
    @BindView(R.id.unbind_service)
    Button mUnbindService;

    private DemoServiceConnection mConnection = new DemoServiceConnection();
    private IDemoAidlService mIDemoAidlService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bind_service, R.id.unbind_service})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bind_service:
                Logger.i(TAG, "onClick :: bind_service");
                Intent intent = new Intent("com.tustar.demo.AIDL");
                intent.setPackage("com.tustar.demo");
                bindService(intent, mConnection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                Logger.i(TAG, "onClick :: unbind_service");
                unbindService(mConnection);
                break;
            default:
                break;
        }
    }

    class DemoServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.i(TAG, "onServiceConnected :: " + "name = [" + name
                    + "], service = [" + service + "]");

            mIDemoAidlService = IDemoAidlService.Stub.asInterface(service);
            try {
                int result = mIDemoAidlService.plus(1, 2);
                String upperStr = mIDemoAidlService.toUpperCase("hello");
                Logger.d(TAG, "onServiceConnected :: result = " + result);
                Logger.d(TAG, "onServiceConnected :: upperStr = " + upperStr);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.i(TAG, "onServiceDisconnected :: " + "name = [" + name + "]");
        }
    }
}
