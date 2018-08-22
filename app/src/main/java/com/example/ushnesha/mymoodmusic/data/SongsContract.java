package com.example.ushnesha.mymoodmusic.data;

import android.net.Uri;

public class SongsContract {
    public static final String CONTENT_AUTHORITY = "com.example.ushnesha.mymoodmusic";
    public static final Uri BASE_URI = Uri.parse("content://com.example.ushnesha.mymoodmusic");

    interface ItemsColumns {
        String _ID = "_id";
        String SONG_NAME = "song_name";
        String ALBUM_NAME = "album_name";
        String ARTIST_NAME = "artist_name";
        String SONG_URL = "song_url";
        String PLAY_BACK_SECS = "playback_secs";
    }

    public static class Items implements ItemsColumns {
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.example.ushnesha.mymoodmusic.items";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.example.ushnesha.mymoodmusic.items";

        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath("items").build();
        }

        public static Uri buildItemUri(long _id) {
            return BASE_URI.buildUpon().appendPath("items").appendPath(Long.toString(_id)).build();
        }

        public static long getItemId(Uri itemUri) {
            return Long.parseLong(itemUri.getPathSegments().get(1));
        }
    }

    private SongsContract() {
    }
}
