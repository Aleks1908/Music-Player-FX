module com.musicplayer.musicplayerfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
            
                            
    opens com.musicplayer.musicplayerfx to javafx.fxml;
    exports com.musicplayer.musicplayerfx;
}