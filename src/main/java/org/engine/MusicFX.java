package org.engine;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Objects;

public enum MusicFX {

    SOLAR_SAILOR ("/music/solar_sailor.mp3"),
    CANT_GET_OUTTA_THE_RAIN ("/music/cant_get_outta_the_rain.mp3");

    private final MediaPlayer mediaPlayer;

    MusicFX(String audioFile) {
        Media music = new Media(Objects.requireNonNull(getClass().getResource(audioFile)).toExternalForm());
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(1);
    }

    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    public void setCycleCount(int duration) {
        mediaPlayer.setCycleCount(duration);
    }

    public void play() {
        mediaPlayer.play();
    }

    public boolean isPlaying() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public boolean isStopped() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED;
    }

    public void pause() {mediaPlayer.pause();}

    public void stop() {
        mediaPlayer.pause();
        mediaPlayer.seek(new Duration(0));
        mediaPlayer.stop();

    }

    public void backward() {mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(1)));}
    public void forward() {mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(1)));}

}
