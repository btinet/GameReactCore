package org.gamereact.module;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.effect.Bloom;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.engine.Fonts;
import org.engine.TangibleObject;
import org.engine.Track;
import org.gamereact.component.ReactButton;

import java.io.File;
import java.util.ArrayList;

public abstract class MultimediaModule extends ControllableModule {

    Rectangle fill = new Rectangle(300, 80, new Color(0.4, 0.6, 0.8, .2));
    Rectangle frame = new Rectangle(300, 80, Color.TRANSPARENT);
    Rectangle statusField = new Rectangle(400, 40, Color.TRANSPARENT);
    Rectangle statusFillField = new Rectangle(400, 40, new Color(0.4, 0.6, 0.8, .2).darker());
    MediaPlayer mediaPlayer;
    ArrayList<Track> tracks;
    Group trackGroup = new Group();
    Media media = null;
    ReactButton prevButton = new ReactButton("back", "jam-set-backward-square");
    ReactButton stopButton = new ReactButton("pause", "jam-pause");
    ReactButton playButton = new ReactButton("play", "jam-play-square");
    ReactButton nextButton = new ReactButton("next", "jam-set-forward-square");
    ReactButton toggleTrackViewButton = new ReactButton("toggleTrackView", "jam-info");
    Slider slTime = new Slider();
    Boolean trackView = false;
    Text title = new Text("Eins und Alles");
    Text time = new Text("23:45 | 24:14");

    public MultimediaModule(TangibleObject tangibleObject, String title, String file, ArrayList<Track> tracks) {
        super(tangibleObject);
        this.setConnectable(true);
        this.title.setText(title);
        File mediaFile = new File(resources + file);
        media = new Media(mediaFile.toURI().toString());

        this.mediaPlayer = new MediaPlayer(media);

        this.tracks = tracks;

        setTime();

        this.mediaPlayer.setOnEndOfMedia(this::stop);
        this.mediaPlayer.setOnStopped(this::updateButtons);

        this.mediaPlayer.currentTimeProperty().addListener(ov -> {
            if (!slTime.isValueChanging()) {
                setTime();
            }
            for (Track track :
                    tracks) {
                track.setActive(mediaPlayer.getCurrentTime());
            }
            updateButtons();
        });

        slTime.valueProperty().addListener(ov -> {
            if (slTime.isValueChanging()) {
                this.mediaPlayer.seek(new Duration(slTime.getValue()));
            }
        });

        this.trackGroup.getChildren().addAll(this.tracks);
        this.trackGroup.setTranslateX(-140);
        this.trackGroup.setTranslateY(-80);
        this.trackGroup.setOpacity(0);

        int trackCount = this.tracks.size();

        for (int i = 0; i < trackCount; i++) {

            this.tracks.get(i).setTranslateY(-(trackCount - i) * 50);
            if (i % 2 == 0) {
                ((Rectangle) this.tracks.get(i).getChildren().get(0)).setFill(new Color(0.4, 0.6, 0.8, .2));
            }
        }

        this.slTime.setPrefWidth(300);
        this.slTime.setTranslateX(-40);
        this.slTime.setTranslateY(60);

        this.time.setFill(new Color(1, 1, 1, .9));
        this.time.setTextAlignment(TextAlignment.RIGHT);
        this.time.setTranslateY(-74);
        this.time.setTranslateX(135);
        this.time.setFont(Fonts.REGULAR_14.getFont());

        this.title.setFill(new Color(1, 1, 1, .9));
        this.title.setTranslateY(-74);
        this.title.setTranslateX(-130);
        this.title.setFont(Fonts.BOLD_16.getFont());

        this.statusField.setStroke(new Color(0.4, 0.6, 0.8, 1));

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

        this.frame.setStroke(new Color(0.4, 0.6, 0.8, 1));

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
        addCancelConnectionButton();
        getChildren().add(this.fill);
        getChildren().add(this.trackGroup);
        getChildren().add(this.statusFillField);
        getChildren().add(this.statusField);
        getChildren().add(this.title);
        getChildren().add(this.time);
        getChildren().add(this.slTime);
        getChildren().addAll(this.buttonList);
    }

    public void play() {
        mediaPlayer.play();
        updateButtons();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public boolean isPlaying() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public boolean isStopped() {
        return mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED;
    }

    public void pause() {
        mediaPlayer.pause();
        updateButtons();
    }

    public void stop() {
        mediaPlayer.pause();
        mediaPlayer.seek(new Duration(0));
        mediaPlayer.stop();
        updateButtons();

    }

    public void rewind() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(1)));
        updateButtons();
    }

    public void forward() {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(1)));
        updateButtons();
    }

    public void gotoAndPlay(Duration startDuration) {
        play();
        mediaPlayer.seek(startDuration);
        updateButtons();
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    protected void updateButtons() {
        if (isPlaying()) {
            playButton.setEnabled(false);
            stopButton.setEnabled(true);
        } else {
            playButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
        prevButton.setEnabled(
                mediaPlayer.getCurrentTime().greaterThan(new Duration(0))
        );
        nextButton.setEnabled(
                !mediaPlayer.getCurrentTime().greaterThanOrEqualTo(mediaPlayer.getTotalDuration())
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
        int t = (int) time;
        if (t > 9) {
            return String.valueOf(t);
        }
        return "0" + t;
    }

    protected void setTime() {
        double total = this.mediaPlayer.getTotalDuration().toMillis();
        double current = this.mediaPlayer.getCurrentTime().toMillis();
        slTime.setMax(total);
        slTime.setValue(current);
        time.setText(getTimeString(current) + "/" + getTimeString(total));
    }

    protected void enableToggleTrackViewButton() {
        toggleTrackViewButton.setEnabled(true);
        updateButtons();
    }

    protected void toggleTrackButtons() {
        for (Track track :
                this.tracks) {
            track.getPlayButton().setEnabled(!track.getPlayButton().isEnabled());
        }
    }

    public void toggleTrackView() {

        int trackCount = this.tracks.size();

        toggleTrackViewButton.setEnabled(false);
        updateButtons();
        if (trackView) {
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
            tt.setToY(statusField.getTranslateY() + 65 * trackCount);
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
            tt.setToY(statusField.getTranslateY() - 65 * trackCount);
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

    @Override
    public void doAction() {

    }
}
