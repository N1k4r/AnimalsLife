module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens com.example.project to javafx.fxml;
    exports com.example.project;
}