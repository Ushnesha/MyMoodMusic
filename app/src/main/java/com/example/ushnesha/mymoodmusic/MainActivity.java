package com.example.ushnesha.mymoodmusic;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.ushnesha.mymoodmusic.Models.SongDetail;
import com.example.ushnesha.mymoodmusic.remote.Config;
import com.example.ushnesha.mymoodmusic.remote.RemoteEndpointUtil;
import com.example.ushnesha.mymoodmusic.utils.MusicAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public final String TAG=MainActivity.class.getName();
    ArrayList<SongDetail> song_list;
    private SeekBar seekBar;
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private MediaPlayer mediaPlayer;
    private Handler handler= new Handler();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView) findViewById(R.id.recycler);
        seekBar=(SeekBar) findViewById(R.id.seekBar);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
//        SongDetail s=new SongDetail("Sawan Aya Hai", "Arijit Singh", "http://listen.vo.llnwd.net/g3/4/7/5/9/1/1402419574.mp3");
//        song_list.add(s);


        if(isCheckConnection()){
            new MyAsync().execute();
        }else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isCheckConnection()){
                    new MyAsync().execute();
                }else{
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public class MyThread extends Thread{
        @Override
        public void run() {
            while (true) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(mediaPlayer != null) {
                    seekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        }
                    });
                }


            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    private void fillArrayList(){

        try {
            JSONArray array = RemoteEndpointUtil.fetchJsonArray();
            if (array == null) {
                throw new JSONException("Invalid parsed item array");
            }

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String playbackSecs = object.getString("playbackSeconds");
                String songName = object.getString("name");
                String artistName = object.getString("artistName");
                String albumName = object.getString("albumName");
                String songUrl = object.getString("previewURL");
                SongDetail sd = new SongDetail(songName, artistName, songUrl, albumName, playbackSecs);
                song_list.add(sd);


            }
        }catch (JSONException e) {
            Log.e(TAG, "Error updating content.", e);
        }

    }


    private boolean isCheckConnection(){
        boolean ch=false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni == null || !ni.isConnected()) {
                Log.e(TAG, "Not online, not refreshing.");
                ch=false;
            }else{
                ch=true;
            }
        }else{
            Log.e(TAG, "cm is null");
            ch=false;
        }
        return ch;
    }


    public class MyAsync extends AsyncTask<Void,Void,JSONArray>{


        @Override
        protected JSONArray doInBackground(Void... voids) {
            JSONArray jsonString=null;
            jsonString=RemoteEndpointUtil.fetchJsonArray();
            return jsonString;
        }

        @Override
        protected void onPostExecute(JSONArray arr) {

            try {
//                JSONObject obj=new JSONObject(s);

                song_list=new ArrayList<SongDetail>();
                if(arr != null) {
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject a = arr.getJSONObject(i);
                        String playbackSec = a.getString("playbackSeconds");
                        String songName = a.getString("name");
                        String artistName = a.getString("artistName");
                        String albumName = a.getString("albumName");
                        String songUrl = a.getString("previewURL");
                        Log.e(TAG, playbackSec + ' ' + songName + ' ' + artistName + ' ' + albumName + ' ' + songUrl);
                        SongDetail sd = new SongDetail(songName, artistName, songUrl, albumName, playbackSec);
                        song_list.add(sd);
                    }
                }else{
                    Log.e(TAG,"JSONArray is null");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            musicAdapter=new MusicAdapter(MainActivity.this, song_list);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation() );
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(musicAdapter);


            musicAdapter.setOnitemClickListener(new MusicAdapter.OnitemClickListener() {
                @Override
                public void onItemClick(final Button b, View v, final SongDetail obj, int pos) {
                    if (b.getText().toString().equals("Stop")) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                        b.setText("Play");
                    } else {
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ProgressDialog progressDialog = null;
                                    progressDialog = ProgressDialog.show(MainActivity.this, "", "Playing...");
                                    mediaPlayer = new MediaPlayer();
                                    mediaPlayer.setDataSource(obj.getSongUrl());
                                    mediaPlayer.prepareAsync();
                                    final ProgressDialog finalProgressDialog = progressDialog;
                                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            finalProgressDialog.cancel();
                                            mp.start();
                                            seekBar.setProgress(0);
                                            seekBar.setMax(mp.getDuration());
                                        }
                                    });
                                    b.setText("Stop");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        handler.postDelayed(r, 100);
                    }

                }
            });

            Thread t = new MyThread();
            t.start();

        }
    }


}
