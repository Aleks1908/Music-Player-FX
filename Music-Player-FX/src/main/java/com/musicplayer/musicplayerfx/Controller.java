package com.musicplayer.musicplayerfx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    @FXML
    private Label songNameLabel;

    @FXML
    private Label currentSecond;

    @FXML
    private Label songLength;

    private File[] files;
    private int currentFileIndex = 0;

    private String formatDuration(Duration duration) {
        int seconds = (int) duration.toSeconds();
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void chooseFileMethod(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File folder = directoryChooser.showDialog(null);
        if (folder != null) {
            files = folder.listFiles((dir, name) -> name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".mp4"));
            if (files != null && files.length > 0) {
                Media media = new Media(files[0].toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                songNameLabel.setText(files[0].getName());

                DoubleProperty widthProp = mediaView.fitWidthProperty();
                DoubleProperty heightProp = mediaView.fitHeightProperty();

                widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

                mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                    progressBar.setValue(newValue.toSeconds());
                    currentSecond.setText(formatDuration(newValue));
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
                    songNameLabel.setText(files[0].getName());
                    songLength.setText(formatDuration(total));

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
                    songNameLabel.setText(files[0].getName());
                    mediaPlayer.play();


                });
            }
        }
    }

    private void newSong() {
        Media media = new Media(files[currentFileIndex].toURI().toString());
        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(() -> {
            Duration total = media.getDuration();
            progressBar.setMax(total.toSeconds());
            songNameLabel.setText(files[0].getName());
            songLength.setText(formatDuration(total));
            mediaView.setMediaPlayer(mediaPlayer);
            songNameLabel.setText(files[currentFileIndex].getName());
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                progressBar.setValue(newValue.toSeconds());
                currentSecond.setText(formatDuration(newValue));
            });
            mediaPlayer.play();
        });
    }

    public void play(ActionEvent event) {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        } else{
            mediaPlayer.play();
            mediaPlayer.setRate(1);
        }

    }

    public void nextMedia(ActionEvent event) {
        if (currentFileIndex < files.length - 1) {
            currentFileIndex++;
            newSong();
        }
    }

    public void prevMedia(ActionEvent event) {
        if (currentFileIndex > 0) {
            currentFileIndex--;
            newSong();
        }
    }
}