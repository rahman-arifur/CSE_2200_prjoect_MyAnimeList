package com.example.cse_2200_prjoect_myanimelist;

import javafx.beans.property.*;

public class Anime {
    private final StringProperty name;
    private final StringProperty rating;
    private final IntegerProperty episodes;
    private final IntegerProperty releaseYear;
    private final StringProperty genre;

    public Anime(String name, String rating, int episodes, int releaseYear, String genre) {
        this.name = new SimpleStringProperty(name);
        this.rating = new SimpleStringProperty(rating);
        this.episodes = new SimpleIntegerProperty(episodes);
        this.releaseYear = new SimpleIntegerProperty(releaseYear);
        this.genre = new SimpleStringProperty(genre);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty ratingProperty() {
        return rating;
    }

    public IntegerProperty episodesProperty() {
        return episodes;
    }

    public IntegerProperty releaseYearProperty() {
        return releaseYear;
    }

    public StringProperty genreProperty() {
        return genre;
    }
}