# MyAnimeList Project

## Overview
This project is a Java-based application that allows users to manage their anime watchlist. It utilizes basic Object-Oriented Programming (OOP) concepts, CRUD operations with a MySQL database, JSON parsing using a third-party API, and basic UI/UX design with CSS and JavaFX.

## Features
- **User Authentication**: Users can log in and log out.
- **Anime Management**: Users can add, view, and remove anime from their watchlist.
- **Anime Details**: Fetch and display detailed information about anime using a third-party API.
- **Responsive UI**: Designed with JavaFX and styled using CSS for a better user experience.

## Technologies Used
- **Java**: Core programming language.
- **JavaFX**: For building the user interface.
- **SceneBuilder**: For designing the UI.
- **MySQL**: Database for storing user and anime information.
- **Gson**: For JSON parsing.
- **CSS**: For styling the UI.

## Setup Instructions
1. **Clone the Repository**:
    ```sh
    git clone https://github.com/rahman-arifur/myanimelist.git
    cd myanimelist
    ```

2. **Database Setup**:
    - Install MySQL and create a database named `project2200`.
    - Run the following SQL script to create the necessary tables:
      ```sql
      CREATE TABLE users (
          id INT AUTO_INCREMENT PRIMARY KEY,
          username VARCHAR(50) NOT NULL,
          password VARCHAR(50) NOT NULL
      );

      CREATE TABLE animeinfo (
          id INT AUTO_INCREMENT PRIMARY KEY,
          name VARCHAR(100) NOT NULL,
          rating VARCHAR(10),
          episodes INT,
          release_year INT,
          genre VARCHAR(50)
      );

      CREATE TABLE user_anime (
          user_no INT,
          anime_id INT,
          FOREIGN KEY (user_no) REFERENCES users(id),
          FOREIGN KEY (anime_id) REFERENCES animeinfo(id)
      );
      ```

3. **Configure Database Connection**:
    - Update the database connection details in `src/main/java/com/example/cse_2200_prjoect_myanimelist/User.java`:
      ```java
      private static final String url = "jdbc:mysql://localhost:3306/project2200";
      private static final String user = "root";
      private static final String pass = "your_password";
      ```

4. **Build and Run the Project**:
    - Open the project in IntelliJ IDEA.
    - Build the project using Maven.
    - Run the `Main` class to start the application.

## Usage
- **Login**: Enter your username and password to log in.
- **Add Anime**: Click on the "Add Anime" button to add a new anime to your watchlist.
- **View Anime Details**: Double-click on an anime in the table to view its details.
- **Remove Anime**: Select an anime and click the "Remove" button to delete it from your watchlist.

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.

## Acknowledgements
- **Jikan API**: For providing anime data.
- **Gson**: For JSON parsing.
- **JavaFX**: For the UI framework.
- **SceneBuilder**: For the UI design tool.

## Contact
For any questions or feedback, please contact rahman-arifur on GitHub.
