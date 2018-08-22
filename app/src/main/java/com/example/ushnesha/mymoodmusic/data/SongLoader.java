package com.example.ushnesha.mymoodmusic.data;

import android.content.ContentProviderResult;
import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

public class SongLoader extends CursorLoader {

    public static SongLoader newAllSongInstance(Context context){
        return new SongLoader(context, SongsContract.Items.buildDirUri());
    }

    public static SongLoader newInstanceForItemId(Context c, long itemId){
        return new SongLoader(c,SongsContract.Items.buildItemUri(itemId));
    }

    private SongLoader(Context context, Uri uri) {
        super(context,uri, Query.PROJECTION,null,null,null);
    }

    public interface Query{
        String[] PROJECTION = {
                SongsContract.Items._ID,
                SongsContract.Items.SONG_NAME,
                SongsContract.Items.ARTIST_NAME,
                SongsContract.Items.SONG_URL,
                SongsContract.Items.ALBUM_NAME,
                SongsContract.Items.PLAY_BACK_SECS
        };

        int _ID=0;
        int SONG_NAME=1;
        int ARTIST_NAME=2;
        int SONG_URL=3;
        int ALBUM_NAME=4;
        int PLAY_BACK_SECS=5;

    }

}
