package org.gamereact.module;


import com.tuio.TuioCursor;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.effect.Bloom;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.engine.TangibleObject;
import org.gamereact.component.ReactButton;
import org.gamereact.component.ToolBar;
import org.gamereact.gamereactcore.CoreApplication;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ChartModule extends ControllableModule {

    LineChart<Number, Number> chart;
    XYChart.Series<Number, Number> series;
    private final SimpleIntegerProperty lastX = new SimpleIntegerProperty(0);
    private final Random rand = new Random();
    double initialTime = 0;
    double timeSincePause = 0;
    double pauseStart = 0;
    double pauseEnd = 0;
    Boolean pause = true;
    Rectangle fill = new Rectangle(800, 300, new Color(0.1, 0.3, 0.4, .2));

    Rectangle buttonFill = new Rectangle(300, 80, new Color(0.4, 0.6, 0.8, .2));
    ReactButton zoomOutButton = new ReactButton("zoom-out", "ci-subtract-alt");
    ReactButton zoomInButton = new ReactButton("zoom-in", "ci-add-alt");
    ReactButton playButton = new ReactButton("play", "ci-pause-filled");
    final Timeline playButtonToggleAnimation = new Timeline(
            new KeyFrame(Duration.millis(400),
                    actionEvent -> playButton.setEnabled(false)));

    public ChartModule(TangibleObject tangibleObject) {
        super(tangibleObject);
        setConnectable(true);
        this.fill.setStrokeWidth(0);
        this.fill.setArcHeight(20);
        this.fill.setArcWidth(20);
        this.fill.setEffect(new Bloom());

        this.buttonFill.setTranslateY(-40);
        this.buttonFill.setStrokeWidth(0);
        this.buttonFill.setTranslateX(-40);
        this.buttonFill.setArcHeight(20);
        this.buttonFill.setArcWidth(20);
        this.buttonFill.setEffect(new Bloom());

        fill.setTranslateX(-140);
        fill.setTranslateY(-360);

        LineChart<Number, Number> chart = this.createChart();
        series =  new XYChart.Series<>();
        series.setName("Sinus des Drehwinkels");
        chart.getData().clear();
        series.getData().clear();
        chart.getData().add(series);
        Group chartGroup = new Group();
        chartGroup.getChildren().add(chart);
        chartGroup.setTranslateY(-360);
        chartGroup.setTranslateX(-140);

        zoomInButton.setTranslateX(75);
        playButton.setTranslateX(225);
        playButton.setBackground(new Color(0.9, 0.2, 0.5, .4));
        this.buttonList.add(zoomOutButton);
        this.buttonList.add(zoomInButton);
        this.buttonList.add(playButton);

        ToolBar toolBar = new ToolBar();
        toolBar.setTranslateX(320);

        addCancelConnectionButton();
        getChildren().add(buttonFill);
        getChildren().add(fill);
        getChildren().add(toolBar);
        getChildren().add(chartGroup);
        getChildren().addAll(this.buttonList);

        playButtonToggleAnimation.setCycleCount(1);
        playButtonToggleAnimation.setOnFinished(e -> playButton.setEnabled(true));
    }

    private LineChart<Number, Number> createChart() {


        chart = new LineChart<>(new NumberAxis(), new NumberAxis());

        chart.setCreateSymbols(false);
        chart.setStyle("-fx-background-color:  transparent");
        chart.setVerticalGridLinesVisible(true);
        chart.setHorizontalGridLinesVisible(false);
        chart.setAnimated(true);
        chart.setLegendVisible(false);
        chart.setPrefWidth(800);
        chart.setPrefHeight(300);
        ((NumberAxis)chart.getXAxis()).setForceZeroInRange(false);
        ((NumberAxis)chart.getXAxis()).setMinorTickVisible(false);
        chart.getXAxis().setAutoRanging(false);
        ((NumberAxis)chart.getXAxis()).setLowerBound(0);
        ((NumberAxis)chart.getXAxis()).setUpperBound(60);
        ((NumberAxis)chart.getYAxis()).setLowerBound(-1);
        ((NumberAxis)chart.getYAxis()).setUpperBound(1);
        chart.getYAxis().setAutoRanging(false);
        chart.getXAxis().setLabel("t in s");
        chart.getYAxis().setLabel("U in mV");
        chart.getStyleClass().add("thick-chart");
        return chart;
    }

    public void togglePause(double animationDuration) {
        if(!playButtonToggleAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            playButtonToggleAnimation.playFromStart();
            setPause(!pause, animationDuration);
        }

    }

    public Boolean getPause() {
        return pause;
    }

    public void setPause(Boolean pause, double animationDuration) {
        if (pause) {

            pauseStart = animationDuration - initialTime;
            System.out.println("Pausenstart: " + pauseStart);
            System.out.println("=========================");

            playButton.setIcon("play","ci-pause-filled");
            playButton.setBackground(new Color(0.9, 0.2, 0.5, .4));


        } else {

            pauseEnd = animationDuration - initialTime;
            System.out.println("Pausenende: " + pauseEnd);
            System.out.println("Pausenzeit: " + (pauseEnd-pauseStart));
            System.out.println("=========================");

            initialTime += (pauseEnd-pauseStart);
            playButton.setIcon("play","ci-play-filled-alt");
            playButton.setBackground(new Color(0.4, 0.9, 0.5, .4));
        }
        this.pause = pause;
    }

    private void moveRange() {

    }

    public void zoomIn() {
        double upperBound = ((NumberAxis)chart.getXAxis()).getUpperBound();
        ((NumberAxis)chart.getXAxis()).setUpperBound(upperBound-1);
    }

    public void zoomOut() {
        double upperBound = ((NumberAxis)chart.getXAxis()).getUpperBound();
        ((NumberAxis)chart.getXAxis()).setUpperBound(upperBound+1);
    }

    public void resetData() {

    }

    public void updateChart(double time, double s) {
        if(!pause) {
            double mTime = time - initialTime;
            //if (series.getData().size()>1000) series.getData().remove(0);
            Double rndDouble = ThreadLocalRandom.current().nextDouble(-1,1);
            XYChart.Data<Number, Number> data = new XYChart.Data<>(mTime, s);

            series.getData().add(data);
        } else {
            //timeSincePause = time - initialTime;
        }

    }

    public void moveOnXAxis(double sin) {

        double lowerBound = ((NumberAxis)chart.getXAxis()).getLowerBound();
        double upperBound = ((NumberAxis)chart.getXAxis()).getUpperBound();

        if(sin < -.15 || sin > .15) {
            lowerBound += sin*.7;
            upperBound += sin*.7;
        }
        ((NumberAxis)chart.getXAxis()).setLowerBound(lowerBound);
        ((NumberAxis)chart.getXAxis()).setUpperBound(upperBound);
    }

    @Override
    public void doAction() {

        // Aktionen für jede Fingereingabe überprüfen:
        for (Map.Entry<TuioCursor,Circle> finger : getCursorList()) {
            for(ReactButton button : getButtonList()) {
                if (button.isEnabled() && button.intersects(finger.getValue())) {
                    System.out.printf("Treffer auf %s%n", button.getName());
                }
            }
        }

    }
}
