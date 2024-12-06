package com.example.cse_2200_prjoect_myanimelist;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ResourceBundle;

public class User implements Initializable {
    @FXML
    private Button logoutButton;
    private String user_id;
    @FXML
    private Button addAnimeButton;

    @FXML
    private TableView<Anime> animeTable;

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
        user_id = HelloController.getUser_id();
        // Initialize the table columns
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        ratingColumn.setCellValueFactory(cellData -> cellData.getValue().ratingProperty());
        episodesColumn.setCellValueFactory(cellData -> cellData.getValue().episodesProperty().asObject());
        releaseYearColumn.setCellValueFactory(cellData -> cellData.getValue().releaseYearProperty().asObject());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());

        // Load watched animes
        loadWatchedAnimes();

        animeTable.setRowFactory(tv -> {
            TableRow<Anime> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Anime rowData = row.getItem();
                    showAnimeDetails(rowData);
                }
            });
            return row;
        });
    }

    private void showAnimeDetails(Anime anime) {
        String apiUrl = "https://api.jikan.moe/v4/anime?q=" + URLEncoder.encode(anime.nameProperty().get(), StandardCharsets.UTF_8);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                    JsonArray dataArray = jsonObject.getAsJsonArray("data");

                    if (dataArray.size() > 0) {
                        JsonObject animeDetails = dataArray.get(0).getAsJsonObject();

                        Platform.runLater(() -> {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("anime-details-view.fxml"));
                                Scene scene = new Scene(fxmlLoader.load());
                                AnimeDetailsController controller = fxmlLoader.getController();
                                controller.setAnimeDetails(animeDetails);

                                Stage stage = new Stage();
                                Stage currentStage = (Stage) animeTable.getScene().getWindow();
                                stage.setWidth(currentStage.getWidth());
                                stage.setHeight(currentStage.getHeight());
                                stage.setTitle("Anime Details");
                                stage.setScene(scene);
                                stage.setResizable(false);
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        System.out.println("No results found for the search query.");
                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    private void loadWatchedAnimes() {
        String query = "SELECT a.name, a.rating, a.episodes, a.release_year, a.genre " +
                "FROM animeinfo a " +
                "JOIN user_anime ua ON a.id = ua.anime_id " +
                "WHERE ua.user_no = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, getCurrentUserId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Anime anime = new Anime(
                        rs.getString("name"),
                        rs.getString("rating"),
                        rs.getInt("episodes"),
                        rs.getInt("release_year"),
                        rs.getString("genre")
                );
                animeTable.getItems().add(anime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogoutButtonAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.setTitle("MyAnimeList");
        Image icon = new Image(getClass().getResourceAsStream("titleicon.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void handleAddAnimeButtonAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-anime-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Add Anime");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void handleRefreshButtonAction() {
        animeTable.getItems().clear();
        loadWatchedAnimes();
    }

    private Integer getCurrentUserId() {
        return Integer.parseInt(user_id);
    }

    @FXML
    private void handleRemoveAnimeButtonAction() {
        Anime selectedAnime = animeTable.getSelectionModel().getSelectedItem();
        if (selectedAnime != null) {
            removeAnimeFromUserWatchlist(selectedAnime);
            animeTable.getItems().remove(selectedAnime);
        }
    }

    private void removeAnimeFromUserWatchlist(Anime anime) {
        String query = "DELETE FROM user_anime WHERE user_no = ? AND anime_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, getCurrentUserId());
            stmt.setInt(2, AddAnimeController.getAnimeIdByName(anime.nameProperty().get()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}