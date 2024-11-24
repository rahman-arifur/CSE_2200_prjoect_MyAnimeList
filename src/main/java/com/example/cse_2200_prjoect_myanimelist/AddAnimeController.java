package com.example.cse_2200_prjoect_myanimelist;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddAnimeController implements Initializable {
    @FXML
    private TableView<Anime> unwatchedAnimeTable;

    @FXML
    private TableColumn<Anime, String> nameColumn;

    @FXML
    private TableColumn<Anime, String> ratingColumn;

    @FXML
    private TableColumn<Anime, Integer> episodesColumn;

    @FXML
    private TableColumn<Anime, Integer> releaseYearColumn;

    @FXML
    private TableColumn<Anime, String> genreColumn;

    private static final String url = "jdbc:mysql://localhost:3306/project2200";
    private static final String user = "root";
    private static final String pass = "11010347";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the table columns
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty());
        episodesColumn.setCellValueFactory(cellData -> cellData.getValue().episodesProperty().asObject());
        releaseYearColumn.setCellValueFactory(cellData -> cellData.getValue().releaseYearProperty().asObject());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());

        // Load unwatched animes
        loadUnwatchedAnimes();
    }

    private void loadUnwatchedAnimes() {
        String query = "SELECT a.name, a.rating, a.episodes, a.release_year, a.genre " +
                "FROM animeinfo a " +
                "WHERE a.id NOT IN (SELECT ua.anime_id FROM user_anime ua WHERE ua.user_no = ?)";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, getCurrentUserId()); // Replace with actual user ID retrieval logic
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Anime anime = new Anime(
                        rs.getString("name"),
                        rs.getString("rating"),
                        rs.getInt("episodes"),
                        rs.getInt("release_year"),
                        rs.getString("genre")
                );
                unwatchedAnimeTable.getItems().add(anime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private Button addSelectedAnimeButton = new Button();

    @FXML
    private void handleAddSelectedAnimeButtonAction(ActionEvent e) {
        Anime selectedAnime = unwatchedAnimeTable.getSelectionModel().getSelectedItem();
        if (selectedAnime != null) {
            addAnimeToUserWatchlist(selectedAnime);
            ((Stage) addSelectedAnimeButton.getScene().getWindow()).close();

        }
    }

    private void addAnimeToUserWatchlist(Anime anime) {
        String query = "INSERT INTO user_anime (user_no, anime_id) VALUES (?, (SELECT id FROM animeinfo WHERE name = ?))";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, getCurrentUserId()); // Replace with actual user ID retrieval logic
            stmt.setString(2, anime.nameProperty().get());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getCurrentUserId() {
        return Integer.parseInt(HelloController.getUser_id());
    }
    
}