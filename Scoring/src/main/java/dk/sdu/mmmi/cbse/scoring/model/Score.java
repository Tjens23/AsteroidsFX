package dk.sdu.mmmi.cbse.scoring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scores")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private UUID playerID;
    
    @Column(name = "score_value")
    private int score; // Changed from 'value' to 'score'
    
    private LocalDateTime timestamp;

    // Default constructor (required by JPA)
    public Score() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with score value
    public Score(int score) {
        this.score = score;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with player name and score value
    public Score(UUID playerID, int score) {
        this.playerID = playerID;
        this.score = score;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPlayerID() {
        return this.playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", getPlayerID='" + playerID + '\'' +
                ", score=" + score +
                ", timestamp=" + timestamp +
                '}';
    }
}
