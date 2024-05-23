module com.example.discussion_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.persistence;

    opens com.example.discussion_app.entities;
    opens com.example.discussion_app to javafx.fxml;
    exports com.example.discussion_app;
    exports com.example.discussion_app.controller;
    opens com.example.discussion_app.controller to javafx.fxml;
}