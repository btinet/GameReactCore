package org.engine;

import javafx.scene.Group;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.gamereact.component.ReactButton;

public class Track extends Group {

    private final Text title = new Text();
    private final Duration startDuration;
    private final Duration endDuration;
    private final Rectangle background = new Rectangle(400,40,new Color(0.4,0.6,0.8,.2));

    private final ReactButton playButton = new ReactButton("playTrack","jam-arrow-square-right");

    public Track(String title, Duration startDuration, Duration endDuration) {
        this.title.setText(title);
        this.startDuration = startDuration;
        this.endDuration = endDuration;

        this.title.setFill(new Color(1,1,1,.9));
        this.title.setFont(Fonts.BOLD_16.getFont());
        this.title.setTranslateX(20);

        this.playButton.setTranslateX(440);
        this.playButton.setTranslateY(-5);
        this.playButton.setEnabled(false);

        this.background.setArcHeight(20);
        this.background.setArcWidth(20);
        this.background.setTranslateY(-25);
        this.background.setEffect(new Bloom());

        getChildren().addAll(this.background,this.title,this.playButton);
    }

    public ReactButton getPlayButton() {
        return playButton;
    }

    public Text getTitle() {
        return title;
    }

    public void setActive(Duration currentDuration) {
        if(currentDuration.greaterThanOrEqualTo(startDuration) && currentDuration.lessThan(endDuration)) {
            background.setFill(new Color(0.4,0.9,0.5,.4));
        } else {
            background.setFill(new Color(0.4,0.6,0.8,.2));
        }
    }

    public Duration getStartDuration() {
        return startDuration;
    }

    public Duration getEndDuration() {
        return endDuration;
    }
}
