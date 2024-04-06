module com.example.pawnder {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pawnder to javafx.fxml;
    exports com.example.pawnder;
    exports com.example.pawnder.engine;
}