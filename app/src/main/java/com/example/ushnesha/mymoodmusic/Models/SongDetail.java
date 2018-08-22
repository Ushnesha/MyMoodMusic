package com.example.ushnesha.mymoodmusic.Models;

public class SongDetail {

    public String songName,artistName,songUrl,albumName,playback;

    public SongDetail(String songName, String artistName, String songUrl, String albumName, String playback){
        this.songName=songName;
        this.artistName=artistName;
        this.songUrl=songUrl;
        this.albumName=albumName;
        this.playback=playback;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getPlayback() {
        return playback;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setPlayback(String playback) {
        this.playback = playback;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }
}
