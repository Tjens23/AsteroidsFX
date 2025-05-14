package dk.sdu.mmmi.cbse.scoring.service;

import dk.sdu.mmmi.cbse.scoring.model.Score;
import dk.sdu.mmmi.cbse.scoring.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScoreService {
    
    private final ScoreRepository repository;
    
    @Autowired
    public ScoreService(ScoreRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Save a new score to the database
     * @param score the score to save
     * @return the saved score with generated ID
     */
    public Score saveScore(Score score) {
        return repository.save(score);
    }
    
    /**
     * Get the top 10 highest scores
     * @return list of the top 10 scores
     */
    public List<Score> getTopScores() {
        return repository.findTop10ByOrderByScoreDesc();
    }
    
    /**
     * Get all scores for a specific player
     * @param playerID the name of the player
     * @return list of scores for the player
     */
    public List<Score> getScoresByPlayer(UUID playerID) {
        return repository.findByPlayerID(playerID);
    }
    
    /**
     * Get all scores in the database
     * @return list of all scores
     */
    public List<Score> getAllScores() {
        return repository.findAll();
    }
    
    /**
     * Delete all scores from the database
     */
    public void resetScores() {
        repository.deleteAll();
    }

    public List<Score> getHighScores() {
        return repository.findTop10ByOrderByScoreDesc();
    }
}
