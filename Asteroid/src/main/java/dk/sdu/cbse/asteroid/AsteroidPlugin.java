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
        for (int i = 0; i < 5; i++) {
            Entity asteroid = createLargeAsteroid(gameData);
            world.addEntity(asteroid);
        }
    }

    private Entity createLargeAsteroid(GameData gameData) {
        Entity asteroid = new Entity();
        asteroid.setType("Asteroid");
        asteroid.setX(random.nextInt(gameData.getDisplayWidth()));
        asteroid.setY(random.nextInt(gameData.getDisplayHeight()));
        asteroid.setDx((random.nextFloat() - 0.5f) * 2);
        asteroid.setDy((random.nextFloat() - 0.5f) * 2);
        asteroid.setRadius(20);
        asteroid.setLife(3);
        return asteroid;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities()) {
            world.removeEntity(asteroid);
        }
    }
}