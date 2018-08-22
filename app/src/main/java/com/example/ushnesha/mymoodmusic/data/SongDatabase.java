package com.example.ushnesha.mymoodmusic.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.ushnesha.mymoodmusic.data.SongsProvider.Tables;


public class SongDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="mymoodmusic.db";
    private static final int DATABASE_VERSION=2;

    public SongDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+ Tables.ITEMS+"("
                +SongsContract.ItemsColumns._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +SongsContract.ItemsColumns.SONG_NAME+" TEXT NOT NULL,"
                +SongsContract.ItemsColumns.ARTIST_NAME+" TEXT NOT NULL,"
                +SongsContract.ItemsColumns.ALBUM_NAME+" TEXT NOT NULL,"
                +SongsContract.ItemsColumns.SONG_URL+" TEXT NOT NULL,"
                +SongsContract.ItemsColumns.PLAY_BACK_SECS+" TEXT NOT NULL"+ ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Tables.ITEMS);
        onCreate(db);
    }
}
