/**

 * File: Controller.java

 * Author: Aleksanadar Ivanov, Mohamed Ben Yahia

 * Date: 23/04/2023

 */

package com.musicplayer.musicplayerfx;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    private File previouslyPlayed;

    private int currentFileIndex = 0;

    public void chooseFileMethod(ActionEvent event) {               //This function is executed when the user selects valid files and it initializes the mediaPlayer and mediaView
        DirectoryChooser directoryChooser = new DirectoryChooser(); //it also sets up all the visual elements such as labels and sliders
        File folder = directoryChooser.showDialog(null);

        if (folder != null) {           //here we check the folder for valid files
            files = folder.listFiles((dir, name) -> name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".mp4"));
            if(files.length <= 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Alert");
                String s ="Please select a folder that contains files with one of the following formats: .mp3/.wav/.mp4 ";
                alert.setContentText(s);
                alert.show();
            }
            if (files != null && files.length > 0) {    //if files are valid we initialize mediaView and mediaPlayer
                Media media = new Media(files[0].toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                songNameLabel.setText(files[0].getName());

                DoubleProperty widthProp = mediaView.fitWidthProperty();
                DoubleProperty heightProp = mediaView.fitHeightProperty();

                widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

                mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {     //here we set up a listener for the progressbar and the current second label
                    progressBar.setValue(newValue.toSeconds());
                    currentSecond.setText(formatDuration(newValue));
                });

                progressBar.setOnMousePressed(e -> {        //here we setup a listener for pressing on the slider so that it can seek to this place in the song
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                });

                progressBar.setOnMouseDragged(e -> {        //here we setup a listener for dragging on the slider so that it can seek to this place in the song
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
                });

                mediaPlayer.setOnReady(() -> {
                    Duration total = media.getDuration();
                    progressBar.setMax(total.toSeconds());
                    songNameLabel.setText(files[0].getName());
                    songLength.setText(formatDuration(total));

                });

                volumeSlider.setValue(mediaPlayer.getVolume() * 100);       //here we setup a listener for the volume slider
                volumeSlider.valueProperty().addListener((observable) -> {
                    mediaPlayer.setVolume(volumeSlider.getValue() / 100);
                });

                mediaPlayer.play();

                mediaPlayer.setOnEndOfMedia(() -> {     //here it checks when a song ends it automatically plays the next one
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
                    mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                        progressBar.setValue(newValue.toSeconds());
                        currentSecond.setText(formatDuration(newValue));
                    });
                    mediaPlayer.play();


                });
            }
        }
    }

    private String formatDuration(Duration duration) {      //this method's main functionality is to format the seconds from the song and fomrat them in the correct way for the label
        int seconds = (int) duration.toSeconds();
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void newSong() {        //this method is reliable for playing the next song in the queue
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

    public void play(ActionEvent event) {       //this method sets the status of the song
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
        } else{
            mediaPlayer.play();
            mediaPlayer.setRate(1);
        }

    }

    public void nextMedia(ActionEvent event) {      //this method play next song when the next button is pressed
        if (currentFileIndex < files.length - 1) {
            currentFileIndex++;
            newSong();
        }
    }

    public void prevMedia(ActionEvent event) {      //this method play next song when the previous song button is pressed
        if (currentFileIndex > 0) {
            currentFileIndex--;
            newSong();
        }
    }


    public void playRandomSong() {              //this method play next song when the shuffle button is pressed
        if (files != null && files.length > 0) {
            File randomFile;
            do {
                int randomIndex = (int) (Math.random() * files.length);
                randomFile = files[randomIndex];
            } while (randomFile.equals(previouslyPlayed));

            Media media = new Media(randomFile.toURI().toString());
            mediaPlayer.stop();

            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            songNameLabel.setText(randomFile.getName());
            mediaPlayer.play();

            previouslyPlayed = randomFile;
        }
    }
}