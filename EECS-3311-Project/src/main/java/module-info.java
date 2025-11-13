module org.openjfx.EECS_3311_Project {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.openjfx.EECS_3311_Project to javafx.fxml;
    exports org.openjfx.EECS_3311_Project;
}
