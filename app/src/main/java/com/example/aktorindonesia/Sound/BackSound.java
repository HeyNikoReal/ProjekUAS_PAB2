package com.example.aktorindonesia.Sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.aktorindonesia.R;

public class BackSound {
    private static MediaPlayer mediaPlayer;

    public static void start(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.latarmusik);
            mediaPlayer.setLooping(true);
        }

        mediaPlayer.start();
    }

    public static void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
}
