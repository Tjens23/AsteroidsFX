package dk.sdu.cbse.collision;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;

import java.util.Random;

public class CollisionDetector implements IPostEntityProcessingService {
    private final Random random = new Random();

    @Override
    public void process(GameData gameData, World world) {
        // Check all entities against all other entities for collisions
        for (Entity entity : world.getEntities()) {
            for (Entity other : world.getEntities()) {
                if (entity != other && checkCollision(entity, other)) {
                    handleCollision(entity, other, world);
                }
            }
        }
    }

    private boolean checkCollision(Entity entity1, Entity entity2) {
        // Simple circle collision using Pythagoras
        float dx = entity1.getX() - entity2.getX();
        float dy = entity1.getY() - entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return distance < (entity1.getRadius() + entity2.getRadius());
    }

    private void handleCollision(Entity entity1, Entity entity2, World world) {
        // Handle specific collision cases
        if (entity1.getType().equals("Bullet") && entity2.getType().equals("Asteroid")) {
            handleBulletAsteroidCollision(entity1, entity2, world);
        }
        else if (entity1.getType().equals("Player") && entity2.getType().equals("Asteroid")) {
            handlePlayerAsteroidCollision(entity1, entity2, world);
        }
        else if (entity1.getType().equals("Bullet") && entity2.getType().equals("Player")) {
            // Only handle bullet-player collision if the bullet wasn't fired by player
            Entity shooter = getBulletOwner(entity1);
            if (shooter != null && !"Player".equals(shooter.getType())) {
                handleBulletPlayerCollision(entity1, entity2, world);
            }
        }
        else if (entity1.getType().equals("Bullet") && entity2.getType().equals("Enemy")) {
            // Only handle bullet-enemy collision if the bullet was fired by player
            Entity shooter = getBulletOwner(entity1);
            if (shooter != null && "Player".equals(shooter.getType())) {
                handleBulletEnemyCollision(entity1, entity2, world);
            }
        }
    }

    // Method to determine who fired a bullet (would need bullet ownership tracking)
    private Entity getBulletOwner(Entity bullet) {
        // In a real implementation, bullets would track their owner
        // For now, this is a placeholder
        return null;
    }

    private void handleBulletPlayerCollision(Entity bullet, Entity player, World world) {
        world.removeEntity(bullet);
        player.setLife(player.getLife() - 1);

        if (player.getLife() <= 0) {
            world.removeEntity(player);
        }
    }

    private void handleBulletEnemyCollision(Entity bullet, Entity enemy, World world) {
        world.removeEntity(bullet);
        enemy.setLife(enemy.getLife() - 1);

        if (enemy.getLife() <= 0) {
            world.removeEntity(enemy);
        }
    }

    private void handleBulletAsteroidCollision(Entity bullet, Entity asteroid, World world) {
        // Remove bullet
        world.removeEntity(bullet);

        // Reduce asteroid life or split it
        asteroid.setLife(asteroid.getLife() - 1);

        if (asteroid.getLife() <= 0) {
            if (asteroid.getRadius() > 10) {
                // Split into smaller asteroids
                createSmallerAsteroids(asteroid, world);
            }
            world.removeEntity(asteroid);
        }
    }

    private void createSmallerAsteroids(Entity original, World world) {
        for (int i = 0; i < 2; i++) {
            Entity small = new Entity();
            small.setType("Asteroid");
            small.setX(original.getX());
            small.setY(original.getY());
            small.setDx(original.getDx() + (random.nextFloat() - 0.5f) * 2);
            small.setDy(original.getDy() + (random.nextFloat() - 0.5f) * 2);
            small.setRadius(original.getRadius() / 2);
            small.setLife(original.getLife());
            world.addEntity(small);
        }
    }

    private void handlePlayerAsteroidCollision(Entity player, Entity asteroid, World world) {
        // Reduce player life
        player.setLife(player.getLife() - 1);

        if (player.getLife() <= 0) {
            world.removeEntity(player);
        }
    }
}