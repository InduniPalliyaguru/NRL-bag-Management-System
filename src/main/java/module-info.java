module lk.ijse.nrlbag {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires java.desktop;
    requires javafx.base;
    requires javafx.graphics;

    opens lk.ijse.nrlbag.controller to javafx.fxml;
    opens lk.ijse.nrlbag.dto to java.base;
    exports lk.ijse.nrlbag;
    exports lk.ijse.nrlbag.controller;
    exports lk.ijse.nrlbag.dto;
}
