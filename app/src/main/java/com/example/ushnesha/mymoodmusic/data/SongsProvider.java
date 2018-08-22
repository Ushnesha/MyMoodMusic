package com.example.ushnesha.mymoodmusic.data;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SongsProvider extends ContentProvider {

    private SQLiteOpenHelper mOpenHelper;

    interface Tables{
        String ITEMS="items";
    }

    private static final int ITEMS=0;
    private static final int ITEMS_ID=1;

    private static final UriMatcher sUriMAtcher= buildUriMatcher();

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String auth= SongsContract.CONTENT_AUTHORITY;
        matcher.addURI(auth, "items", ITEMS);
        matcher.addURI(auth, "items/#", ITEMS_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new SongDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final SelectionBuilder builder = buildSelection(uri);
        Cursor cursor=builder.where(selection,selectionArgs).query(db,projection,sortOrder);
        if(cursor != null){
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return cursor;
    }

    private SelectionBuilder buildSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMAtcher.match(uri);
        return buildSelection(uri, match, builder);
    }

    private SelectionBuilder buildSelection(Uri uri, int match, SelectionBuilder builder) {
        final List<String> paths = uri.getPathSegments();
        switch (match) {
            case ITEMS: {
                return builder.table(Tables.ITEMS);
            }
            case ITEMS_ID: {
                final String _id = paths.get(1);
                return builder.table(Tables.ITEMS).where(SongsContract.Items._ID + "=?", _id);
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

    }


    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        }finally {
            db.endTransaction();
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMAtcher.match(uri);
        switch (match){
            case ITEMS:
                return SongsContract.Items.CONTENT_TYPE;
            case ITEMS_ID:
                return SongsContract.Items.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        final int match=sUriMAtcher.match(uri);
        switch(match){
            case ITEMS: {
                final long _id = db.insertOrThrow(Tables.ITEMS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return SongsContract.Items.buildItemUri(_id);
            }default:{
                throw new UnsupportedOperationException("Unknown uri: "+uri);
            }
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder=buildSelection(uri);
        getContext().getContentResolver().notifyChange(uri,null);
        return builder.where(selection,selectionArgs).delete(db);
    }




    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder=buildSelection(uri);
        getContext().getContentResolver().notifyChange(uri,null);
        return builder.where(selection,selectionArgs).update(db,values);
    }
}
