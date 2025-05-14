package dk.sdu.mmmi.cbse.scoring.repository;

import dk.sdu.mmmi.cbse.scoring.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    
    /**
     * Find the top 10 scores ordered by value in descending order.
     * @return List of the top 10 highest scores
     */
    List<Score> findTop10ByOrderByScoreDesc();
    
    /**
     * Find scores by player name ordered by value in descending order.
     * @param playerID the name of the player
     * @return List of scores for the given player
     */
    List<Score> findByPlayerID(UUID playerID);
}

