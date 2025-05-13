package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.Random;

public class AsteroidProcessor implements IEntityProcessingService {
    private static final Random rnd = new Random();
    private static final double SPAWN_CHANCE = 0.02; // 2% chance per frame to spawn
    private static final int MAX_ASTEROIDS = 8;
    private final AsteroidPlugin asteroidPlugin = new AsteroidPlugin();

    @Override
    public void process(GameData gameData, World world) {
        // Process existing asteroids
        for (Entity entity : world.getEntities(Asteroid.class)) {
            Asteroid asteroid = (Asteroid) entity;
            updateAsteroid(asteroid, gameData);
        }

        // Possibly spawn new asteroid
        if (shouldSpawnNewAsteroid(world)) {
            Entity newAsteroid = asteroidPlugin.createRandomAsteroid(gameData, Asteroid.Size.LARGE);
            world.addEntity(newAsteroid);
            System.out.println("Spawned new asteroid: " + newAsteroid.getID());
        }
    }

    private void updateAsteroid(Asteroid asteroid, GameData gameData) {
        // Speed varies based on asteroid health points as a proxy for size
        float speedMultiplier = getSpeedMultiplierFromHealth(asteroid.getHealthPoints());

        // Calculate movement
        double radians = Math.toRadians(asteroid.getRotation());
        double dx = Math.cos(radians) * asteroid.getSpeed() * speedMultiplier;
        double dy = Math.sin(radians) * asteroid.getSpeed() * speedMultiplier;

        // Update position
        asteroid.setX(asteroid.getX() + dx);
        asteroid.setY(asteroid.getY() + dy);

        // Screen wrapping with buffer based on asteroid size
        wrapAsteroid(asteroid, gameData);
    }

    private float getSpeedMultiplierFromHealth(int healthPoints) {
        // Use health points to determine the size category
        if (healthPoints <= Asteroid.Size.SMALL.getSize()) {
            return 3.0f;
        } else if (healthPoints <= Asteroid.Size.MEDIUM.getSize()) {
            return 2.0f;
        } else {
            return 1.0f;
        }
    }

    private float getSpeedMultiplier(Asteroid.Size size) {
        switch (size) {
            case SMALL:
                return 3.0f;
            case MEDIUM:
                return 2.0f;
            case LARGE:
            default:
                return 1.0f;
        }
    }

    private void wrapAsteroid(Asteroid asteroid, GameData gameData) {
        double x = asteroid.getX();
        double y = asteroid.getY();
        double width = gameData.getDisplayWidth();
        double height = gameData.getDisplayHeight();
        float buffer = asteroid.getRadius();

        // Wrap horizontally
        if (x < -buffer) {
            asteroid.setX(width + buffer);
        } else if (x > width + buffer) {
            asteroid.setX(-buffer);
        }

        // Wrap vertically
        if (y < -buffer) {
            asteroid.setY(height + buffer);
        } else if (y > height + buffer) {
            asteroid.setY(-buffer);
        }
    }

    private boolean shouldSpawnNewAsteroid(World world) {
        int currentAsteroids = world.getEntities(Asteroid.class).size();
        return currentAsteroids < MAX_ASTEROIDS && rnd.nextDouble() < SPAWN_CHANCE;
    }
}