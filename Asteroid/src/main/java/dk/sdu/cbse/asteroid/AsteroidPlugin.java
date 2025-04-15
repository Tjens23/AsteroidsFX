package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;

import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {
    private final Random random = new Random();

    @Override
    public void start(GameData gameData, World world) {
        // Create 4 large asteroids
        for (int i = 0; i < 4; i++) {
            Entity asteroid = createLargeAsteroid(gameData);
            world.addEntity(asteroid);
        }
    }

    private Entity createLargeAsteroid(GameData gameData) {
        Entity asteroid = new Asteroid(3); // Large asteroid

        float x = random.nextFloat() * gameData.getDisplayWidth();
        float y = random.nextFloat() * gameData.getDisplayHeight();

        asteroid.setX(x);
        asteroid.setY(y);
        asteroid.setDx((random.nextFloat() * 2 - 1) * 30);
        asteroid.setDy((random.nextFloat() * 2 - 1) * 30);

        // Create irregular polygon shape for asteroid
        asteroid.setPolygonCoordinates(-15, -15, 15, -8, 10, 15, -5, 10);
        return asteroid;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Asteroid) {
                world.removeEntity(entity);
            }
        }
    }
}