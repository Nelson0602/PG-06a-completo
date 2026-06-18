module ucr.lab.pg06a {
    requires javafx.controls;
    requires javafx.fxml;

    opens ucr.lab.pg06a to javafx.fxml;
    exports ucr.lab.pg06a;
}