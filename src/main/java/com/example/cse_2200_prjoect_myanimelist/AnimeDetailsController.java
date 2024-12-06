package com.example.cse_2200_prjoect_myanimelist;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AnimeDetailsController {
    @FXML
    private ImageView animeImage;
    @FXML
    private Label animeName;
    @FXML
    private Label animeRating;
    @FXML
    private Label animeEpisodes;
    @FXML
    private Label animeDescription;
    @FXML
    private Label animeGenre;

    public void setAnimeDetails(JsonObject animeDetails) {
        animeName.setText(animeDetails.has("title") ? animeDetails.get("title").getAsString() : "N/A");
        animeRating.setText(animeDetails.has("score") ? "Rating: " + animeDetails.get("score").getAsString() : "Rating: N/A");
        animeEpisodes.setText(animeDetails.has("episodes") ? "Episodes: " + animeDetails.get("episodes").getAsString() : "Episodes: N/A");
        animeDescription.setText(animeDetails.has("synopsis") ? "Description: " + animeDetails.get("synopsis").getAsString() : "Description: N/A");

        if (animeDetails.has("genres") && animeDetails.get("genres").getAsJsonArray().size() > 0) {
            StringBuilder genres = new StringBuilder("Genre: ");
            animeDetails.get("genres").getAsJsonArray().forEach(genre -> {
                genres.append(genre.getAsJsonObject().get("name").getAsString()).append(", ");
            });
            animeGenre.setText(genres.substring(0, genres.length() - 2));
        } else {
            animeGenre.setText("Genre: N/A");
        }

        if (animeDetails.has("images") && animeDetails.get("images").getAsJsonObject().has("jpg")) {
            String imageUrl = animeDetails.get("images").getAsJsonObject().get("jpg").getAsJsonObject().get("image_url").getAsString();
            animeImage.setImage(new Image(imageUrl));
        } else {
            animeImage.setImage(null);
        }
    }
}