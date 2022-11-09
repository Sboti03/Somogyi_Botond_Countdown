module hu.petrik.countdown.countdown {
    requires javafx.controls;
    requires javafx.fxml;


    opens hu.petrik.countdown.countdown to javafx.fxml;
    exports hu.petrik.countdown.countdown;
}