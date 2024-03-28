package com.nnjtrading.standify;

public class Songs {

    private String songName, songArtist, imageUri;
    private boolean isPlaying;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public boolean isIsplaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public Songs(String songName, String songArtist, String imageUri, boolean isPlaying) {
        this.songName = songName;
        this.songArtist = songArtist;
        this.imageUri = imageUri;
        this.isPlaying = isPlaying;
    }

    public Songs() {

    }
}
