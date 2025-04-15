package dk.sdu.cbse;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages ={
        "dk.sdu.cbse.player",
        "dk.sdu.cbse.enemy",
        "dk.sdu.cbse.asteroid",
        "dk.sdu.cbse.collision",
        "dk.sdu.cbse.bullet"
})
public class AppConfig {
}
