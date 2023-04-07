module com.musicplayer.musicplayerfx {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.musicplayer.musicplayerfx to javafx.fxml;
    exports com.musicplayer.musicplayerfx;
}