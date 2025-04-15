package dk.sdu.cbse.player;

import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class PlayerProcessor implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, World world) {
        for (Entity player : world.getEntities()) {
            processMovement(player, gameData);
            processShootingAction(player, gameData, world);
        }
    }

    private void processMovement(Entity player, GameData gameData) {
        // Check if this entity is actually a player
        if (!"Player".equals(player.getType())) {
            return;
        }

        float dt = gameData.getDelta();

        // Current position
        float x = player.getX();
        float y = player.getY();
        float dx = player.getDx();
        float dy = player.getDy();

        // Get player's current rotation
        double rotation = player.getRotation();

        // Handle rotation with LEFT/RIGHT keys
        float rotationSpeed = 5.0f;
        if (gameData.getKeys().isDown(GameKeys.LEFT)) {
            rotation -= rotationSpeed;
        }
        if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
            rotation += rotationSpeed;
        }

        // Handle acceleration with UP key
        float acceleration = 5.0f;
        if (gameData.getKeys().isDown(GameKeys.UP)) {
            dx += (float) (Math.cos(Math.toRadians(rotation)) * acceleration * dt);
            dy += (float) (Math.sin(Math.toRadians(rotation)) * acceleration * dt);
        }

        // Apply drag to gradually slow down
        float drag = 0.95f;
        dx *= drag;
        dy *= drag;

        // Update position
        x += dx * dt;
        y += dy * dt;

        // Screen wrapping
        int width = gameData.getDisplayWidth();
        int height = gameData.getDisplayHeight();

        if (x > width) x = 0;
        if (x < 0) x = width;
        if (y > height) y = 0;
        if (y < 0) y = height;

        // Update player entity
        player.setX(x);
        player.setY(y);
        player.setDx(dx);
        player.setDy(dy);
        player.setRotation(rotation);
    }

    private void processShootingAction(Entity player, GameData gameData, World world) {
        // Check if this entity is actually a player
        if (!"Player".equals(player.getType())) {
            return;
        }

        // Look for a BulletSPI service
        for (BulletSPI bulletService : getBulletServices()) {
            if (gameData.getKeys().isPressed(GameKeys.SPACE)) {
                // Create a bullet using the service
                Entity bullet = bulletService.createBullet(player, gameData);
                world.addEntity(bullet);
            }
        }
    }

    private Collection<BulletSPI> getBulletServices() {
        return ServiceLoader.load(BulletSPI.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }
}