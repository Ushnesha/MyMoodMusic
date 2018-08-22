package com.example.ushnesha.mymoodmusic.data;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.ushnesha.mymoodmusic.MainActivity;

public class UpdaterService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    Context context;
    private static final String TAG="UpdaterService";
    public static final String BROADCAST_ACTION_STATE_CHANGE="com.example.ushnesha.mymoodmusic.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING="com.example.ushnesha.mymoodmusic.intent.extra.REFRESHING";

    public UpdaterService(Context context) {
        super(TAG);
        this.context=context;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ConnectivityManager cm=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if(cm!=null){
            NetworkInfo ni=cm.getActiveNetworkInfo();
            if(ni==null || !ni.isConnected()){
                Toast.makeText(context, "No Intenet Connection", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"Mobile not connected to internet");
                return;
            }else{
                Log.e(TAG, "cm is null");
            }

            sendBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));
        }
    }

}
