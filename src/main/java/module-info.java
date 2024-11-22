module com.example.cse_2200_prjoect_myanimelist {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.cse_2200_prjoect_myanimelist to javafx.fxml;
    exports com.example.cse_2200_prjoect_myanimelist;
}