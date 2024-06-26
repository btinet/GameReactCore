package org.gamereact.module;


import com.tuio.TuioCursor;
import com.tuio.TuioObject;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.engine.Controller;
import org.engine.FingerTouchObject;
import org.engine.TangibleObject;
import org.gamereact.component.ReactButton;
import org.gamereact.component.ReactButtonAction;
import org.gamereact.component.ToolBar;

import java.util.HashMap;
import java.util.Map;

public class ChartModule extends ControllableModule {

    LineChart<Number, Number> chart;
    XYChart.Series<Number, Number> series;
    double initialTime = 0;
    double pauseStart = 0;
    double pauseEnd = 0;
    Boolean pause = true;
    Rectangle fill = new Rectangle(800, 300, new Color(0.1, 0.3, 0.4, .2));

    Rectangle buttonFill = new Rectangle(300, 80, new Color(0.4, 0.6, 0.8, .2));
    ReactButton zoomOutButton = new ReactButton(ReactButtonAction.ZOOM_OUT, "ci-subtract-alt");
    ReactButton zoomInButton = new ReactButton(ReactButtonAction.ZOOM_IN, "ci-add-alt");
    ReactButton playButton = new ReactButton(ReactButtonAction.PLAY, "ci-pause-filled");
    final Timeline playButtonToggleAnimation = new Timeline(
            new KeyFrame(Duration.millis(400),
                    actionEvent -> playButton.setEnabled(false)));
    ToolBar toolBar;

    public ChartModule(TangibleObject tangibleObject) {
        super(tangibleObject);
        setConnectable(true);

        zoomOutButton.setPull(false);
        zoomInButton.setPull(false);

        this.fill.setStrokeWidth(0);
        this.fill.setArcHeight(20);
        this.fill.setArcWidth(20);

        this.buttonFill.setTranslateY(-40);
        this.buttonFill.setStrokeWidth(0);
        this.buttonFill.setTranslateX(50);
        this.buttonFill.setArcHeight(20);
        this.buttonFill.setArcWidth(20);

        fill.setTranslateX(50);
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
        chartGroup.setTranslateX(50);

        zoomOutButton.setTranslateX(85);
        zoomInButton.setTranslateX(160);
        playButton.setTranslateX(225);
        playButton.setBackground(new Color(0.9, 0.2, 0.5, .4));
        this.buttonList.add(zoomOutButton);
        this.buttonList.add(zoomInButton);
        this.buttonList.add(playButton);

        toolBar = new ToolBar();
        toolBar.setTranslateX(420);

        addCancelConnectionButton();
        getChildren().add(buttonFill);
        getChildren().add(fill);
        getChildren().add(toolBar);
        getChildren().add(chartGroup);
        getChildren().add(rotationGroup);
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
        ((NumberAxis)chart.getYAxis()).setForceZeroInRange(false);
        ((NumberAxis)chart.getXAxis()).setMinorTickVisible(false);
        chart.getXAxis().setAutoRanging(false);
        ((NumberAxis)chart.getXAxis()).setLowerBound(0);
        ((NumberAxis)chart.getXAxis()).setUpperBound(60);
        //((NumberAxis)chart.getYAxis()).setLowerBound(0);
        //((NumberAxis)chart.getYAxis()).setUpperBound(1500);
        chart.getYAxis().setAutoRanging(true);
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

    public void setPause(Boolean pause, double animationDuration) {
        if (pause) {
            pauseStart = animationDuration - initialTime;
            playButton.setIcon(ReactButtonAction.PLAY,"ci-pause-filled");
            playButton.setBackground(new Color(0.9, 0.2, 0.5, .4));
        } else {
            pauseEnd = animationDuration - initialTime;
            initialTime += (pauseEnd-pauseStart);
            playButton.setIcon(ReactButtonAction.PLAY,"ci-play-filled-alt");
            playButton.setBackground(new Color(0.4, 0.9, 0.5, .4));
        }
        this.pause = pause;
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

    public void updateChart(double time, HashMap<String,Double> s) {
        for (Map.Entry<String,Double> set :
             s.entrySet()) {

            // TODO: Für jedes EntrySet eine eigene chart series

            if(!pause && set.getValue() != null) {
                double mTime = time - initialTime;
                XYChart.Data<Number, Number> data = new XYChart.Data<>(mTime, set.getValue());
                series.getData().add(data);
            }
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
    public void doAction(double animationDuration) {
        for (Map.Entry<TuioCursor, FingerTouchObject> finger : Controller.cursorList.entrySet()) {
            for(ReactButton button : getButtonList()) {
                if (button.isEnabled() && button.intersects(finger.getValue())) {
                    switch (button.getName()) {
                        case ZOOM_IN:
                            zoomIn();
                            break;
                        case ZOOM_OUT:
                            zoomOut();
                            break;
                        case PLAY:
                            togglePause(animationDuration);
                            break;
                        case CANCEL:
                            disconnect();
                            break;
                        default:
                            break;
                    }
                }
            }

            for(ReactButton button : toolBar.getButtonList()) {
                // TODO: Buttonaktion nach Release ausführen. Aber wie?
                if (button.isEnabled() && button.intersects(finger.getValue())) {
                    switch (button.getName()) {
                        case ARDUINO_TOGGLE:
                            System.out.println("Arduino!");
                            break;
                        case CIRCUIT:
                            System.out.println("Circuit!");
                            break;
                        case MAXIMUM:
                            System.out.println("Maximum!");
                            break;
                        case MINIMUM:
                            System.out.println("Minimum!");
                            break;
                        case AVERAGE:
                            System.out.println("Average!");
                            break;
                        default:
                            break;
                    }
                }
            }

        }
    }

    @Override
    public void onTuioObjectRemoved(TuioObject tobj) {
        resetData();
    }

}
