package com.example.akbar.tradebooks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    NetworkChangeReceiver receiver;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiver=new NetworkChangeReceiver();
        registerReceiver(receiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ConnectivityManager manager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        boolean isConnected=activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
        if(isConnected)
        {
            Intent intent=new Intent(getApplicationContext(),LoginPage.class);
            startActivity(intent);
        }
        else
        {

        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getExtras()!=null)
            {
                NetworkInfo info=(NetworkInfo)intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(info!=null && info.getState()== NetworkInfo.State.CONNECTED)
                {

                    Intent intent2=new Intent(MainActivity.this,LoginPage.class);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }
}
