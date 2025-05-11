/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author jcs
 */
class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final List<IGamePluginService> gamePluginServices;
    private final List<IEntityProcessingService> entityProcessingServiceList;
    private final List<IPostEntityProcessingService> postEntityProcessingServices;
    
    // Game state variables
    private boolean gameOver = false;
    private int score = 0;
    private String playerName = "Player1";
    private VBox scorePanel;
    private Text scoreText;
    private Text gameOverText;
    private Text notificationText;
    private List<Text> topScoreTexts;
    private int sessionHighScore = 0;
    private RestTemplate restTemplate = new RestTemplate();
    private static final String SCORING_SERVICE_URL = "http://localhost:8080/api/scores";
    private static final int MAX_TOP_SCORES = 3;

    Game(List<IGamePluginService> gamePluginServices, List<IEntityProcessingService> entityProcessingServiceList, List<IPostEntityProcessingService> postEntityProcessingServices) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessingServiceList = entityProcessingServiceList;
        this.postEntityProcessingServices = postEntityProcessingServices;
        this.topScoreTexts = new ArrayList<>();
    }

    public void start(Stage window) throws Exception {
        // Prompt for player name
        promptForPlayerName();
        
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        
        // Create score panel with top scores
        setupScorePanel();
        
        // Create notification text (initially invisible)
        notificationText = new Text(10, 50, "");
        notificationText.setFill(Color.YELLOW);
        notificationText.setFont(new Font(16));
        notificationText.setVisible(false);
        gameWindow.getChildren().add(notificationText);
        
        // Create game over text
        gameOverText = new Text(gameData.getDisplayWidth()/2, gameData.getDisplayHeight()/2, 
                "GAME OVER\nPress R to restart");
        gameOverText.setFill(Color.WHITE);
        gameOverText.setFont(new Font(30));
        gameOverText.setTextAlignment(TextAlignment.CENTER);
        gameOverText.setVisible(false);
        gameOverText.setTranslateX(-100); // Center the text
        gameWindow.getChildren().add(gameOverText);

        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, true);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
            if (event.getCode().equals(KeyCode.R)) {
                if (gameOver) {
                    restartGame();
                }
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, false);
            }

        });

        // Lookup all Game Plugins using ServiceLoader
        for (IGamePluginService iGamePlugin : getGamePluginServices()) {
            iGamePlugin.start(gameData, world);
        }
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }
        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();
    }

    public void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver) {
                    update();
                }
                draw();
                gameData.getKeys().update();
            }

        }.start();
    }
    
    /**
     * Restarts the game by resetting the world, game state, and plugins
     */
    private void restartGame() {
        // Submit score to scoring service
        submitScore();
        
        // Clear current entities
        for (Entity entity : world.getEntities()) {
            world.removeEntity(entity);
        }
        polygons.clear();
        gameWindow.getChildren().removeIf(node -> node instanceof Polygon);
        
        // Reset game state
        gameOver = false;
        score = 0;
        scoreText.setText("Destroyed asteroids: 0");
        gameOverText.setVisible(false);
        notificationText.setVisible(false);
        
        // Reset session high score
        sessionHighScore = 0;
        
        // Refresh top scores
        fetchTopScores();
        
        // Restart all game plugins
        for (IGamePluginService iGamePlugin : getGamePluginServices()) {
            iGamePlugin.start(gameData, world);
        }
        
        // Add new entities to the window
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }
    }
    
    /**
     * Submits the final score to the scoring service
     */
    private void submitScore() {
        try {
            // Create score object (adapt to your actual Score class as needed)
            Map<String, Object> scoreData = new ConcurrentHashMap<>();
            scoreData.put("player", playerName);
            scoreData.put("score", score);
            
            // Post score to the scoring service
            ResponseEntity<String> response = restTemplate.postForEntity(
                SCORING_SERVICE_URL, scoreData, String.class);
            
            System.out.println("Score submitted: " + response.getBody());
            
            // Update top scores display after submission
            fetchTopScores();
            
            // Show submission confirmation
            showNotification("Score submitted successfully!", Color.GREEN);
        } catch (Exception e) {
            System.err.println("Failed to submit score: " + e.getMessage());
            
            // Show error notification
            showNotification("Failed to submit score!", Color.RED);
        }
    }
    
    /**
     * Shows a notification message with fade-out animation
     * @param message the message to display
     * @param color the color of the message
     */
    private void showNotification(String message, Color color) {
        notificationText.setText(message);
        notificationText.setFill(color);
        notificationText.setVisible(true);
        
        FadeTransition ft = new FadeTransition(Duration.seconds(3), notificationText);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(evt -> notificationText.setVisible(false));
        ft.play();
    }
    
    /**
     * Sets up the score panel with current score and top scores display
     */
    private void setupScorePanel() {
        // Create score panel container
        scorePanel = new VBox(10); // 10px spacing between elements
        scorePanel.setPadding(new Insets(10));
        
        // Add semi-transparent background
        BackgroundFill bgFill = new BackgroundFill(
            Color.rgb(0, 0, 0, 0.6), // Semi-transparent black
            new CornerRadii(5),      // Slightly rounded corners
            Insets.EMPTY
        );
        scorePanel.setBackground(new Background(bgFill));
        
        // Position in top-right corner
        scorePanel.setTranslateX(gameData.getDisplayWidth() - 220);
        scorePanel.setTranslateY(20);
        scorePanel.setMinWidth(200);
        
        // Create current score text
        scoreText = new Text("Destroyed asteroids: 0");
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(new Font(18));
        
        // Create separator
        Text separator = new Text("---------------------------");
        separator.setFill(Color.GRAY);
        
        // Create top scores header
        Text topScoresHeader = new Text("TOP SCORES");
        topScoresHeader.setFill(Color.GOLD);
        topScoresHeader.setFont(new Font(16));
        
        // Add elements to panel
        scorePanel.getChildren().add(scoreText);
        scorePanel.getChildren().add(separator);
        scorePanel.getChildren().add(topScoresHeader);
        
        // Create placeholders for top scores
        for (int i = 0; i < MAX_TOP_SCORES; i++) {
            Text topScoreText = new Text((i + 1) + ". ---");
            topScoreText.setFill(Color.LIGHTGRAY);
            topScoreText.setFont(new Font(14));
            topScoreTexts.add(topScoreText);
            scorePanel.getChildren().add(topScoreText);
        }
        
        // Add panel to game window
        gameWindow.getChildren().add(scorePanel);
        
        // Fetch initial top scores
        fetchTopScores();
    }
    
    /**
     * Fetches top scores from the scoring service
     */
    private void fetchTopScores() {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(
                SCORING_SERVICE_URL + "/top", List.class);
            updateTopScoresDisplay(response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to fetch top scores: " + e.getMessage());
            // Set placeholder text for top scores
            for (int i = 0; i < topScoreTexts.size(); i++) {
                topScoreTexts.get(i).setText((i + 1) + ". ---");
            }
        }
    }
    
    /**
     * Updates the top scores display with data from the scoring service
     */
    private void updateTopScoresDisplay(List scores) {
        if (scores == null || scores.isEmpty()) {
            for (int i = 0; i < topScoreTexts.size(); i++) {
                topScoreTexts.get(i).setText((i + 1) + ". No scores");
            }
            return;
        }
        
        for (int i = 0; i < Math.min(scores.size(), MAX_TOP_SCORES); i++) {
            Map<String, Object> scoreData = (Map<String, Object>) scores.get(i);
            String name = (String) scoreData.get("playerName");
            Integer value = (Integer) scoreData.get("score");
            
            if (name == null) name = "Unknown";
            
            topScoreTexts.get(i).setText(String.format("%d. %s: %d", 
                i + 1, name, value));
                
            // Highlight if this is the current player
            if (name.equals(playerName)) {
                topScoreTexts.get(i).setFill(Color.YELLOW);
            } else {
                topScoreTexts.get(i).setFill(Color.LIGHTGRAY);
            }
        }
        
        // Fill any remaining slots with placeholders
        for (int i = scores.size(); i < MAX_TOP_SCORES; i++) {
            topScoreTexts.get(i).setText((i + 1) + ". ---");
            topScoreTexts.get(i).setFill(Color.LIGHTGRAY);
        }
    }
    
    /**
     * Handles game over state
     */
    public void setGameOver() {
        if (!gameOver) {
            gameOver = true;
            gameOverText.setVisible(true);
            System.out.println("GAME OVER");
        }
    }
    
    /**
     * Increments the score when an asteroid is destroyed
     */
    public void incrementScore() {
        score++;
        scoreText.setText("Destroyed asteroids: " + score);
        
        // Check for session high score
        if (score > sessionHighScore) {
            sessionHighScore = score;
            
            // Check if this score might make it to the high score list
            boolean possibleTopScore = false;
            if (topScoreTexts.size() > 0 && !topScoreTexts.get(0).getText().contains("---")) {
                // Parse the lowest top score to compare
                String lastScoreText = topScoreTexts.get(topScoreTexts.size() - 1).getText();
                int colonIndex = lastScoreText.lastIndexOf(":");
                
                try {
                    if (colonIndex > 0) {
                        String scoreValue = lastScoreText.substring(colonIndex + 1).trim();
                        int lowestTopScore = Integer.parseInt(scoreValue);
                        possibleTopScore = score > lowestTopScore;
                    }
                } catch (Exception e) {
                    // If parsing fails, assume it's not a top score
                    possibleTopScore = false;
                    System.err.println("Error parsing score: " + e.getMessage());
                }
            }
            
            // Show appropriate notifications for high score
            if (possibleTopScore) {
                // Add extra animation for high score
                ScaleTransition st = new ScaleTransition(Duration.millis(300), scoreText);
                st.setFromX(1.0);
                st.setFromY(1.0);
                st.setToX(1.5);
                st.setToY(1.5);
                st.setCycleCount(4);
                st.setAutoReverse(true);
                st.play();
                
                // Show notification with direct color parameter
                showNotification("Potential High Score!", Color.GOLD);
            } else {
                // Regular animation for session best
                animateScoreText();
                
                // Show notification with direct color parameter
                showNotification("New Session Best: " + score + "!", Color.YELLOW);
            }
        } else {
            // Regular score increment animation
            animateScoreText();
        }
        
        // Check for milestone achievements (every 10 points)
        if (score % 10 == 0) {
            showMilestoneAchievement();
        }
    }
    /**
     * Animates the score text to provide visual feedback
     */
    private void animateScoreText() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), scoreText);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }
    
    /**
     * Shows a notification for milestone achievements
     */
    private void showMilestoneAchievement() {
        showNotification("MILESTONE: " + score + " asteroids destroyed!", Color.GOLD);
    }
    
    /**
     * Prompts the player to enter their name
     */
    private void promptForPlayerName() {
        TextInputDialog dialog = new TextInputDialog("Player1");
        dialog.setTitle("Asteroids");
        dialog.setHeaderText("Welcome to Asteroids!");
        dialog.setContentText("Please enter your name:");
        
        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                playerName = name.trim();
            }
        });
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, world);
        }
        
        // Check for game over condition in collision detection
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.process(gameData, world);
            
            // Check if player entity is gone (player was hit)
            boolean playerExists = false;
            for (Entity entity : world.getEntities()) {
                if (entity.getEntityType() != null && entity.getEntityType().equalsIgnoreCase("PLAYER")) {
                    playerExists = true;
                }
            }
            
            // If player is gone, set game over
            if (!playerExists && !gameOver) {
                setGameOver();
            }
        }
    }

    private void draw() {
        for (Entity polygonEntity : polygons.keySet()) {
            if (!world.getEntities().contains(polygonEntity)) {
                Polygon removedPolygon = polygons.get(polygonEntity);
                polygons.remove(polygonEntity);
                gameWindow.getChildren().remove(removedPolygon);
            }
        }

        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
        }

    }

    public List<IGamePluginService> getGamePluginServices() {
        return gamePluginServices;
    }

    public List<IEntityProcessingService> getEntityProcessingServices() {
        return entityProcessingServiceList;
    }

    public List<IPostEntityProcessingService> getPostEntityProcessingServices() {
        return postEntityProcessingServices;
    }

}
