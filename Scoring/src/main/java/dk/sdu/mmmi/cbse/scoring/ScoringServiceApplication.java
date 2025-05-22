package dk.sdu.mmmi.cbse.scoring;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@CrossOrigin
public class ScoringServiceApplication {

    private int score = 0;

    public static void main(String[] args) {
        SpringApplication.run(ScoringServiceApplication.class, args);
    }

    @GetMapping("/score/get")
    public int getScore() {
        return this.score;
    }

    @PutMapping("/score/add/{score}")
    public int addToScore(@PathVariable(value = "score") int score) {
        return this.score +=score;
    }

    @PutMapping("/score/set/{score}")
    public int updateScore(@PathVariable(value = "score") int score) {
        return this.score = score;
    }
}