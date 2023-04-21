# Music-Player-FX

## How to setup project

1.Make sure to have JavaFX 20 downloaded on your pc

2.Locate JavaFX lib folder from the console on your pc with the following command:
```bash
java --module-path  ...(path to javafx)/javafx-sd-20/lib 
``` 
3. Add the required modules for initalizing the project with the following command: 
```bash
--add-modules javafx.media,javafx.fxml,javafx.controls
```
4. Start the project from the jar file located in the following folder: 
```bash
...(path to project)/out/artifacts/Music_Player_FX_jar/Music-Player-FX.jar
```

## Project description

We have developed a music player in Java that allows users to play, pause, seek tracks and play previous, next and random song. The application have a modern user interface that is user-friendly and a media viewer if the song has a video.

## Features

User-friendly interface: The application has an easy-to-use interface that allows users to easily navigate through their music library and play their favorite tracks.

Import songs: The application allows users can select a folder from their personal computer with songs.

Audio playback controls: The appplication allows users to control the songs that are being played by buttons and sliders also they can change the volume of the played content.

Video playback: If the song that is selected is an mp4 file the video will automatically play in the application. 

## Technologies

JavaFX: The application us build using JavaFX framework, which provides a rich set of graphical user interface controls and tools for building desktop applications.

Media library: The application uses the media library included in JavaFX to handle audio and video playback.


## Implemented classes

We have implemented two classes. Application class and Controller class. The Application class is responsible for setting up the stage the player is going to be placed in and it loads the FXML file with the styleing of the project. The Controller class is responsible for everything media connected. Importing songs, playing/pausing songs, skipping reversing songs, sliders, labels and media viewer. We also have a FXML file with the whole UI and CSS file for styling some of the elements in the UI.

## Design desicions 

We decided to have only one controller class because JavaFX allows only one controller class to be linked to a FXML file.

## Limitations 

The music player only plays: .mp3, .mp4, .wav media.
