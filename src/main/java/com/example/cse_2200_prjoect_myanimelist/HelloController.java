package com.example.cse_2200_prjoect_myanimelist;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    // Database connection and query logic
    private static final String url = "jdbc:mysql://localhost:3306/project2200";
    private static final String user = "root";
    private static final String pass = "11010347";
    @FXML
    private TextField email;
    private static String user_id;
    @FXML
    private Button login;

    @FXML
    private PasswordField password;

    @FXML
    private ImageView sideimage;

    @FXML
    private Button signup;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    private void handleSignUpButtonAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signup.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) signup.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.show();
    }
    @FXML
    private void handleLoginButtonAction() {
        String userEmail = email.getText();
        String userPassword = password.getText();

        String query = "SELECT * FROM userinfo WHERE user_email = ? AND user_password = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userEmail);
            stmt.setString(2, userPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("user_password");
                if (storedPassword.equals(userPassword)) {
                    user_id = rs.getString("user_no");
                    // Password matches, proceed to the next scene or action
                    //System.out.println(user_id);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Stage stage = (Stage) login.getScene().getWindow();
//                    Image icon = new Image(getClass().getResourceAsStream("usericon.png")); // path to the icon
//                    stage.getIcons().add(icon);
                    stage.setScene(scene);
                    stage.setTitle(userEmail + "'s" +" Dashboard");
                    stage.show();
                } else {
                    // Password does not match
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Wrong Password");
                }
            } else {
                // Email not found
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Email not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static String getUser_id(){
        return user_id;
    }

}