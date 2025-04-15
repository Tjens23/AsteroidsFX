package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;

public class AsteroidControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            if (!(entity instanceof Asteroid)) {
                continue;
            }

            // Random movement for asteroids
            float dt = gameData.getDelta();

            // Move asteroid
            float x = entity.getX();
            float y = entity.getY();

            x += entity.getDx() * dt;
            y += entity.getDy() * dt;

            // Screen wrapping
            if (x > gameData.getDisplayWidth()) x = 0;
            if (x < 0) x = gameData.getDisplayWidth();
            if (y > gameData.getDisplayHeight()) y = 0;
            if (y < 0) y = gameData.getDisplayHeight();

            entity.setX(x);
            entity.setY(y);
        }
    }
}