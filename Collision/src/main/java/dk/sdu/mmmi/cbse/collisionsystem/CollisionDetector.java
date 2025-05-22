package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.enemy.Enemy;
import dk.sdu.mmmi.cbse.main.Game;
import dk.sdu.mmmi.cbse.main.Main;
import dk.sdu.mmmi.cbse.playersystem.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class CollisionDetector implements IPostEntityProcessingService {
    private IAsteroidSplitter asteroidSplitter;
    public CollisionDetector() {
        // Load the asteroid splitter service
        ServiceLoader<IAsteroidSplitter> loader = ServiceLoader.load(IAsteroidSplitter.class);
        if (loader.findFirst().isPresent()) {
            asteroidSplitter = loader.findFirst().get();
        }
    }


    @Override
    public void process(GameData gameData, World world) {
        // Store entities to be removed to avoid concurrent modification
        List<Entity> entitiesToRemove = new ArrayList<>();
        List<Asteroid> asteroidsToSplit = new ArrayList<>();

        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {
                if (entity1.getID().equals(entity2.getID())) {
                    continue;
                }

                if (collides(entity1, entity2)) {
                    handleCollision(entity1, entity2, world, entitiesToRemove, asteroidsToSplit);
                }
            }
        }

        // Remove entities after processing all collisions
        for (Entity entity : entitiesToRemove) {
            world.removeEntity(entity);
            System.out.println("Removed entity: " + entity.getID());
        }

        // Split asteroids after removing entities
        if (asteroidSplitter != null) {
            for (Asteroid asteroid : asteroidsToSplit) {
                System.out.println("Splitting asteroid with health points: " + asteroid.getHealthPoints());
                asteroidSplitter.splitAsteroid(asteroid, world);
            }
        }
    }

    private void handleCollision(Entity entity1, Entity entity2, World world,
                                 List<Entity> entitiesToRemove, List<Asteroid> asteroidsToSplit) {
        // Handle Player-Asteroid collision
        if ((entity1 instanceof Player && entity2 instanceof Asteroid) ||
                (entity1 instanceof Asteroid && entity2 instanceof Player)) {
            entitiesToRemove.add(entity1);
            entitiesToRemove.add(entity2);
            System.out.println("GAME OVER - Player hit by asteroid!");
            Main.getGame().setGameOver();
        }


        if((entity1 instanceof Enemy && entity2 instanceof Asteroid) ||
                (entity1 instanceof Asteroid && entity2 instanceof Enemy)) {
            entitiesToRemove.add(entity1);
            entitiesToRemove.add(entity2);
        }

        // Handle Player-Enemy collision
        else if ((entity1 instanceof Player && entity2 instanceof Enemy) ||
                (entity1 instanceof Enemy && entity2 instanceof Player)) {
            Entity player = (entity1 instanceof Player) ? entity1 : entity2;
            Entity enemy = (entity1 instanceof Enemy) ? entity1 : entity2;
            entitiesToRemove.add(player);
            entitiesToRemove.add(enemy);
            System.out.println("GAME OVER - Player collided with enemy!");
            Main.getGame().setGameOver();

        }
        // Handle Enemy Bullet-Player collision
        else if ((entity1 instanceof Bullet && entity2 instanceof Player) ||
                (entity1 instanceof Player && entity2 instanceof Bullet)) {
            Bullet bullet = (entity1 instanceof Bullet) ? (Bullet) entity1 : (Bullet) entity2;
            Entity player = (entity1 instanceof Player) ? entity1 : entity2;

            // Check if the bullet was fired by an enemy
            if (bullet.getShooter() instanceof Enemy) {
                entitiesToRemove.add(player);
                entitiesToRemove.add(bullet);
                System.out.println("GAME OVER - Player hit by enemy bullet!");
                Main.getGame().setGameOver();

            }
        }
        // Handle Player Bullet-Enemy collision
        else if ((entity1 instanceof Bullet && entity2 instanceof Enemy) ||
                (entity1 instanceof Enemy && entity2 instanceof Bullet)) {
            Bullet bullet = (entity1 instanceof Bullet) ? (Bullet) entity1 : (Bullet) entity2;
            Entity enemy = (entity1 instanceof Enemy) ? entity1 : entity2;

            // Check if the bullet was fired by a player
            if (bullet.getShooter() instanceof Player) {
                entitiesToRemove.add(enemy);
                entitiesToRemove.add(bullet);
                System.out.println("Enemy destroyed by player bullet!");
            }
        }
        // Handle Bullet-Asteroid collision
        else if ((entity1 instanceof Bullet && entity2 instanceof Asteroid) ||
                (entity1 instanceof Asteroid && entity2 instanceof Bullet)) {
            Asteroid asteroid = (entity1 instanceof Asteroid) ? (Asteroid) entity1 : (Asteroid) entity2;
            Entity bullet = (entity1 instanceof Bullet) ? entity1 : entity2;

            asteroidsToSplit.add(asteroid);
            entitiesToRemove.add(bullet);
            entitiesToRemove.add(asteroid);
            Main.getGame().incrementScore(asteroid);
            System.out.println("Bullet hit asteroid: " + asteroid.getID() + " with health points " + asteroid.getHealthPoints());
        }
    }

    public Boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }
}