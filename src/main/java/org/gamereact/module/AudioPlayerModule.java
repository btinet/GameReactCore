package org.gamereact.module;


import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.effect.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.engine.Fonts;
import org.engine.Module;
import org.engine.MusicFX;
import org.engine.Track;
import org.gamereact.component.ReactButton;

import java.util.ArrayList;


public class AudioPlayerModule extends Module {

    ReactButton prevButton = new ReactButton("back","jam-set-backward-square");
    ReactButton stopButton = new ReactButton("pause","jam-pause");
    ReactButton playButton = new ReactButton("play","jam-play-square");
    ReactButton nextButton = new ReactButton("next","jam-set-forward-square");
    ReactButton toggleTrackViewButton = new ReactButton("toggleTrackView","jam-info");
    Slider slTime = new Slider();
    Rectangle fill = new Rectangle(300,80,new Color(0.4,0.6,0.8,.2));
    Rectangle frame = new Rectangle(300,80,Color.TRANSPARENT);
    Rectangle statusField = new Rectangle(400,40,Color.TRANSPARENT);
    Rectangle statusFillField = new Rectangle(400,40,new Color(0.4,0.6,0.8,.2).darker());

    Boolean trackView = false;
    Text title = new Text("Eins und Alles");
    Text time = new Text("23:45 | 24:14");
    MusicFX player = MusicFX.GOETHE_EINS_UND_ALLES;
    ArrayList<Track> tracks = new ArrayList<>();
    Group trackGroup = new Group();

    public void rewind() {
        player.backward();
        updateButtons();
    }

    public void forward() {
        player.forward();
        updateButtons();
    }

    public void play() {
        player.play();
        updateButtons();
    }

    public void gotoAndPlay(Duration startDuration) {
        player.play();
        player.getMediaPlayer().seek(startDuration);
        updateButtons();
    }

    public void pause() {
        player.pause();
        updateButtons();
    }

    public void stop() {
        player.stop();
        updateButtons();
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    private void updateButtons() {
        if(player.isPlaying()) {
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
        } else {
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
        prevButton.setEnabled(
                player.getMediaPlayer().getCurrentTime().greaterThan(new Duration(0))
        );
        nextButton.setEnabled(
                !player.getMediaPlayer().getCurrentTime().greaterThanOrEqualTo(player.getMediaPlayer().getTotalDuration())
        );
    }

    public static String getTimeString(double millis) {
        millis /= 1000;
        String s = formatTime(millis % 60);
        millis /= 60;
        String m = formatTime(millis % 60);
        millis /= 60;
        String h = formatTime(millis % 24);
        return h + ":" + m + ":" + s;
    }

    public static String formatTime(double time) {
        int t = (int)time;
        if (t > 9) { return String.valueOf(t); }
        return "0" + t;
    }

    private void setTime() {
        double total = this.player.getMediaPlayer().getTotalDuration().toMillis();
        double current = this.player.getMediaPlayer().getCurrentTime().toMillis();
        slTime.setMax(total);
        slTime.setValue(current);
        time.setText(getTimeString(current) + "/" + getTimeString(total));
    }

    private void enableToggleTrackViewButton() {
        toggleTrackViewButton.setEnabled(true);
        updateButtons();
    }

    private void toggleTrackButtons() {
        for (Track track:
             this.tracks) {
            track.getPlayButton().setEnabled(!track.getPlayButton().isEnabled());
        }
    }

    public void toggleTrackView() {

        int trackCount = this.tracks.size();

        toggleTrackViewButton.setEnabled(false);
        updateButtons();
        if(trackView) {
            FadeTransition ft = new FadeTransition();
            ft.setNode(this.trackGroup);
            ft.setAutoReverse(false);
            ft.setCycleCount(1);
            ft.setDuration(new Duration(500));
            ft.setFromValue(1);
            ft.setToValue(0);

            TranslateTransition tt = new TranslateTransition();
            tt.setFromX(statusField.getTranslateX());
            tt.setFromY(statusField.getTranslateY());
            tt.setToX(statusField.getTranslateX());
            tt.setToY(statusField.getTranslateY()+65*trackCount);
            tt.setCycleCount(1);
            tt.setAutoReverse(false);
            tt.setDuration(new Duration(500));
            tt.setNode(statusField);
            tt.setOnFinished(e -> enableToggleTrackViewButton());
            ft.setOnFinished(e -> toggleTrackButtons());
            tt.play();
            ft.play();
        } else {
            FadeTransition ft = new FadeTransition();
            ft.setNode(this.trackGroup);
            ft.setAutoReverse(false);
            ft.setCycleCount(1);
            ft.setDuration(new Duration(500));
            ft.setFromValue(0);
            ft.setToValue(1);

            TranslateTransition tt = new TranslateTransition();
            tt.setFromX(statusField.getTranslateX());
            tt.setFromY(statusField.getTranslateY());
            tt.setToX(statusField.getTranslateX());
            tt.setToY(statusField.getTranslateY()-65*trackCount);
            tt.setCycleCount(1);
            tt.setAutoReverse(false);
            tt.setDuration(new Duration(500));
            tt.setNode(statusField);
            tt.setOnFinished(e -> enableToggleTrackViewButton());
            ft.setOnFinished(e -> toggleTrackButtons());
            tt.play();
            ft.play();
        }

        trackView = !trackView;
    }


    public AudioPlayerModule() {
        setTime();

        this.player.getMediaPlayer().setOnEndOfMedia(this::stop);
        this.player.getMediaPlayer().setOnStopped(this::updateButtons);

        this.player.getMediaPlayer().currentTimeProperty().addListener(ov -> {
            if (!slTime.isValueChanging()) {
                setTime();
            }
            updateButtons();
        });

        slTime.valueProperty().addListener(ov -> {
            if (slTime.isValueChanging()) {
                this.player.getMediaPlayer().seek(new Duration(slTime.getValue()));
            }
        });

        Track track1 = new Track("Strophe 1",new Duration(12160),new Duration(36592));
        Track track2 = new Track("Strophe 2",new Duration(37708),new Duration(63814));
        Track track3 = new Track("Strophe 3",new Duration(64372),new Duration(89139));
        Track track4 = new Track("Strophe 4",new Duration(89197),new Duration(117961));
        this.tracks.add(track1);
        this.tracks.add(track2);
        this.tracks.add(track3);
        this.tracks.add(track4);
        this.trackGroup.getChildren().addAll(this.tracks);
        this.trackGroup.setTranslateX(-140);
        this.trackGroup.setTranslateY(-80);
        this.trackGroup.setOpacity(0);

        int trackCount = this.tracks.size();

        for (int i = 0; i<trackCount; i++) {

            this.tracks.get(i).setTranslateY(-(trackCount-i)*50);
            if(i % 2 == 0) {
                ((Rectangle)this.tracks.get(i).getChildren().get(0)).setFill(new Color(0.4,0.6,0.8,.2));
            }
        }

        this.slTime.setPrefWidth(300);
        this.slTime.setTranslateX(-40);
        this.slTime.setTranslateY(60);

        this.time.setFill(new Color(1,1,1,.9));
        this.time.setTextAlignment(TextAlignment.RIGHT);
        this.time.setTranslateY(-74);
        this.time.setTranslateX(135);
        this.time.setFont(Fonts.REGULAR_14.getFont());

        this.title.setFill(new Color(1,1,1,.9));
        this.title.setTranslateY(-74);
        this.title.setTranslateX(-130);
        this.title.setFont(Fonts.BOLD_16.getFont());

        this.statusField.setStroke(new Color(0.4,0.6,0.8,1));

        this.statusField.setTranslateY(-100);
        this.statusField.setStrokeWidth(2);
        this.statusField.setTranslateX(-140);
        this.statusField.setArcHeight(20);
        this.statusField.setArcWidth(20);

        this.toggleTrackViewButton.setTranslateX(300);
        this.toggleTrackViewButton.setTranslateY(-80);

        this.statusFillField.setTranslateY(-100);
        this.statusFillField.setTranslateX(-140);
        this.statusFillField.setArcHeight(20);
        this.statusFillField.setArcWidth(20);

        this.frame.setStroke(new Color(0.4,0.6,0.8,1));

        this.frame.setTranslateY(-40);
        this.frame.setStrokeWidth(2);
        this.frame.setTranslateX(-40);
        this.frame.setArcHeight(20);
        this.frame.setArcWidth(20);

        this.fill.setTranslateY(-40);
        this.fill.setStrokeWidth(0);
        this.fill.setTranslateX(-40);
        this.fill.setArcHeight(20);
        this.fill.setArcWidth(20);

        this.fill.setEffect(new Bloom());
        this.statusFillField.setEffect(new Bloom());

        prevButton.setEnabled(false);
        stopButton.setEnabled(false);

        stopButton.setTranslateX(75);
        playButton.setTranslateX(150);
        nextButton.setTranslateX(225);

        this.buttonList.add(prevButton);
        this.buttonList.add(stopButton);
        this.buttonList.add(playButton);
        this.buttonList.add(nextButton);
        this.buttonList.add(toggleTrackViewButton);

        getChildren().add(this.fill);
        getChildren().add(this.trackGroup);
        getChildren().add(this.statusFillField);
        getChildren().add(this.statusField);
        getChildren().add(this.title);
        getChildren().add(this.time);
        getChildren().add(this.slTime);
        getChildren().addAll(this.buttonList);

    }

}
