package com.example.ushnesha.mymoodmusic.data;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.ushnesha.mymoodmusic.MainActivity;
import com.example.ushnesha.mymoodmusic.Models.SongDetail;
import com.example.ushnesha.mymoodmusic.remote.RemoteEndpointUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UpdaterService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    Context context;
    private static final String TAG = "UpdaterService";
    public static final String BROADCAST_ACTION_STATE_CHANGE = "com.example.ushnesha.mymoodmusic.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING = "com.example.ushnesha.mymoodmusic.intent.extra.REFRESHING";

//    public UpdaterService(Context context) {
//        super(TAG);
//        this.context = context;
//    }

    public UpdaterService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(RemoteEndpointUtil.isCheckConnection(this)){
            sendBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

            ArrayList<ContentProviderOperation> cpo = new ArrayList<>();

            Uri dirUri = SongsContract.Items.buildDirUri();

            cpo.add(ContentProviderOperation.newDelete(dirUri).build());

            try {
                JSONArray arr = RemoteEndpointUtil.fetchJsonArray();
                if (arr == null) {
                    throw new JSONException("JSONArray is not fetched!");
                }
                for (int i = 0; i < arr.length(); i++) {
                    ContentValues cv = new ContentValues();
                    JSONObject a = arr.getJSONObject(i);
                    String playbackSec = a.getString("playbackSeconds");
                    String songName = a.getString("name");
                    String artistName = a.getString("artistName");
                    String albumName = a.getString("albumName");
                    String songUrl = a.getString("previewURL");
                    Log.e(TAG, playbackSec + ' ' + songName + ' ' + artistName + ' ' + albumName + ' ' + songUrl);
                    cv.put(SongsContract.Items.SONG_NAME, songName);
                    cv.put(SongsContract.Items.ARTIST_NAME, artistName);
                    cv.put(SongsContract.Items.SONG_URL, songUrl);
                    cv.put(SongsContract.Items.ALBUM_NAME, albumName);
                    cv.put(SongsContract.Items.PLAY_BACK_SECS, playbackSec);
                    cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(cv).build());
                }

                getContentResolver().applyBatch(SongsContract.CONTENT_AUTHORITY, cpo);

            } catch (JSONException | RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            sendBroadcast(
                    new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
            }
        else {
            Handler handler=new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    //your operation...
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
