package com.musicplayer.musicplayerfx;


import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

import java.io.File;

public class Controller {

    private String path;
    private MediaPlayer mediaPlayer;

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider progressBar;

    @FXML
    private Slider volumeSlider;



    private File[] files;
    private int currentFileIndex = 0;

    public void chooseFileMethod(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File folder = directoryChooser.showDialog(null);
        if (folder != null) {
            files = folder.listFiles((dir, name) -> name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".mp4"));
            if (files != null && files.length > 0) {
                Media media = new Media(files[0].toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);

                DoubleProperty widthProp = mediaView.fitWidthProperty();
                DoubleProperty heightProp = mediaView.fitHeightProperty();

                widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

                mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                    progressBar.setValue(newValue.toSeconds());
                });

                progressBar.setOnMousePressed(e -> {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                });

                progressBar.setOnMouseDragged(e -> {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                });

                mediaPlayer.setOnReady(() -> {
                    Duration total = media.getDuration();
                    progressBar.setMax(total.toSeconds());
                });

                volumeSlider.setValue(mediaPlayer.getVolume() * 100);
                volumeSlider.valueProperty().addListener((observable) -> {
                    mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                });

                mediaPlayer.play();

                mediaPlayer.setOnEndOfMedia(() -> {
                    if (currentFileIndex < files.length - 1) {
                        currentFileIndex++;
                    } else {
                        currentFileIndex = 0;
                    }
                    Media nextMedia = new Media(files[currentFileIndex].toURI().toString());
                    mediaPlayer.stop();
                    mediaPlayer = new MediaPlayer(nextMedia);
                    mediaView.setMediaPlayer(mediaPlayer);
                    mediaPlayer.play();
                });
            }
        }
    }

    public void nextMedia(ActionEvent event) {
        if (currentFileIndex < files.length - 1) {
            currentFileIndex++;
            Media nextMedia = new Media(files[currentFileIndex].toURI().toString());
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(nextMedia);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        }
    }

    public void prevMedia(ActionEvent event) {
        if (currentFileIndex > 0) {
            currentFileIndex--;
            Media prevMedia = new Media(files[currentFileIndex].toURI().toString());
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(prevMedia);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
        }
    }

    public void play(ActionEvent event){
        mediaPlayer.play();
        mediaPlayer.setRate(1);
    }

    public void pause(ActionEvent event){
        mediaPlayer.pause();
    }

    public void stop(ActionEvent event){
        mediaPlayer.stop();
    }

    public void slowRate(ActionEvent event){
        mediaPlayer.setRate(0.5);
    }
    public void fastRate(ActionEvent event){
        mediaPlayer.setRate(1.5);
    }

    public void forward(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
    }
    public void backwards(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
    }

}