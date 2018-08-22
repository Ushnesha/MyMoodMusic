package com.example.ushnesha.mymoodmusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ushnesha.mymoodmusic.Models.SongDetail;
import com.example.ushnesha.mymoodmusic.R;
import com.example.ushnesha.mymoodmusic.data.SongLoader;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{

    private Cursor cursor;
    private Context context;

    OnitemClickListener onitemClickListener;

    public MusicAdapter(Context context, Cursor cursor){
        this.context=context;
        this.cursor=cursor;
    }

    public interface OnitemClickListener{
        void onItemClick(Button b, View v, SongDetail obj, int pos);
    }

    public void setOnitemClickListener(OnitemClickListener onitemClickListener) {
        this.onitemClickListener = onitemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.list_song_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        cursor.moveToPosition(position);
        return cursor.getLong(SongLoader.Query._ID);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        cursor.moveToPosition(position);
        final SongDetail sd=new SongDetail(cursor.getString(SongLoader.Query.SONG_NAME),cursor.getString(SongLoader.Query.ARTIST_NAME),cursor.getString(SongLoader.Query.SONG_URL),cursor.getString(SongLoader.Query.ALBUM_NAME),cursor.getString(SongLoader.Query.PLAY_BACK_SECS));
        holder.songName.setText(sd.getSongName());
        holder.artistName.setText(sd.getArtistName());
        holder.btnAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onitemClickListener != null){
                    onitemClickListener.onItemClick(holder.btnAct, v, sd, position);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView songName;
        private TextView artistName;
        private Button btnAct;

        public ViewHolder(View itemView) {
            super(itemView);
            songName=(TextView) itemView.findViewById(R.id.song_title);
            artistName=(TextView) itemView.findViewById(R.id.artist_name);
            btnAct=(Button) itemView.findViewById(R.id.butAction);

        }
    }
}
