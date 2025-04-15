package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;

public class AsteroidProcessor implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities()) {
            moveAsteroid(asteroid, gameData);
        }
    }

    private void moveAsteroid(Entity asteroid, GameData gameData) {
        // Update position based on velocity
        float x = asteroid.getX() + asteroid.getDx() * gameData.getDelta();
        float y = asteroid.getY() + asteroid.getDy() * gameData.getDelta();

        // Screen wrap-around
        x = wrapAround(x, 0, gameData.getDisplayWidth());
        y = wrapAround(y, 0, gameData.getDisplayHeight());

        asteroid.setX(x);
        asteroid.setY(y);
    }

    private float wrapAround(float value, float min, float max) {
        if (value > max) {
            return min;
        } else if (value < min) {
            return max;
        }
        return value;
    }
}