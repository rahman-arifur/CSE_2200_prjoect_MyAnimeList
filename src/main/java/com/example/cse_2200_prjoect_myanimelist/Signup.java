package com.example.cse_2200_prjoect_myanimelist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Signup {
    private static final String url = "jdbc:mysql://localhost:3306/project2200";
    private static final String user = "root";
    private static final String pass = "11010347";
    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    protected void handleSignUp() {
        String userEmail = email.getText();
        String userPassword = password.getText();


        String checkQuery = "SELECT * FROM userinfo WHERE user_email = ?";
        String insertQuery = "INSERT INTO userinfo (user_email, user_password) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            checkStmt.setString(1, userEmail);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Email already exists
                showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Email already exists");
            } else {
                // Email does not exist, proceed with insertion
                insertStmt.setString(1, userEmail);
                insertStmt.setString(2, userPassword);
                insertStmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "Account created successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBackButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("My AnimeList");
        stage.show();
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
