package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import java.util.Random;

/**
 *
 * @author corfixen
 */
public class AsteroidSplitterImpl implements IAsteroidSplitter {
    private static final Random rnd = new Random();

    @Override
    public void splitAsteroid(Entity targetEntity, World world) {
        ((Asteroid) targetEntity).takeDamage(1);

        if (!checkHealthy((Asteroid) targetEntity)) {
            return;
        }

        int asteroidsSplit = 2;
        Asteroid[] asteroids = new Asteroid[asteroidsSplit];

        for (int i = 0; i < asteroidsSplit; i++) {
            Asteroid asteroid = new Asteroid();

            asteroid.setBoundingCircleRadius(((Asteroid) targetEntity).getBoundingCircleRadius() / 2);

            setAsteroidPolygon(asteroid);
            // TODO: Der smides en error når polygons bliver tegnet. No idea why.

            asteroid.setX(targetEntity.getX());
            asteroid.setY(targetEntity.getY());
            if (i == 0) {
                asteroid.setRotation(Math.random() * 90);
            } else {
                asteroid.setRotation(asteroids[i - 1].getRotation() + 20 + Math.random() * 50);
            }

            asteroid.setHealthPoints(((Asteroid) targetEntity).getHealthPoints());

            asteroids[i] = asteroid;
        }

        for (Asteroid asteroid : asteroids) {
            world.addEntity(asteroid);
        }
    }

    /**
     * Check if asteroid is still healthy after taking damage
     * @param asteroid The asteroid to check
     * @return true if asteroid is still alive, false if it should be removed
     */
    private boolean checkHealthy(Asteroid asteroid) {
        // If health points are <= 0, return false
        return asteroid.getHealthPoints() > 0;
    }

    /**
     * Generate polygon shape for the asteroid
     * @param asteroid The asteroid to set polygon for
     */
    private void setAsteroidPolygon(Asteroid asteroid) {
        // Number of points in the polygon
        int points = 6 + rnd.nextInt(5); // 6-10 points
        double[] coordinates = new double[points * 2];
        double radius = asteroid.getBoundingCircleRadius();
        double variation = 0.3; // 30% variation

        // Generate a rough circle with random variations
        for (int i = 0; i < points; i++) {
            double angle = (2 * Math.PI * i) / points;
            // Add randomness to the radius
            double adjustedRadius = radius * (1.0 - variation + (rnd.nextDouble() * variation * 2));
            coordinates[i * 2] = Math.cos(angle) * adjustedRadius;
            coordinates[i * 2 + 1] = Math.sin(angle) * adjustedRadius;
        }

        // Assuming Entity class has this method to set polygon coordinates
        asteroid.setPolygonCoordinates(coordinates);
        asteroid.setRadius((float)radius); // Set the collision radius
    }
}