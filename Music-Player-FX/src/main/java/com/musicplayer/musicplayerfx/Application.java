/**

 * File: Controller.java

 * Author: Aleksanadar Ivanov

 * Date: 23/04/2023

 */
package com.musicplayer.musicplayerfx;

        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.stage.Stage;
        import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("music-player-gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false); // set the resizable property to false
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}