package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

public class EnemyControlSystem implements IEntityProcessingService {
    private final Random random = new Random();
    private float shootCooldown = 0;

    @Override
    public void process(GameData gameData, World world) {
        shootCooldown -= gameData.getDelta();

        // Find player position for targeting
        Entity player = null;
        for (Entity entity : world.getEntities()) {
            if ("Player".equals(entity.getType())) {
                player = entity;
                break;
            }
        }

        for (Entity enemy : world.getEntities()) {
            if (!(enemy instanceof Enemy)) {
                continue;
            }

            // Movement
            moveEnemy(enemy, gameData);

            // Shooting logic
            if (player != null && shootCooldown <= 0) {
                if (random.nextFloat() < 0.05) { // 5% chance to shoot per frame
                    shootCooldown = 1.0f; // 1 second cooldown
                    shoot(enemy, gameData, world);
                }
            }
        }
    }

    private void moveEnemy(Entity enemy, GameData gameData) {
        float dt = gameData.getDelta();

        // Random direction change occasionally
        if (random.nextFloat() < 0.01) {
            enemy.setDx(random.nextFloat() * 2 - 1);
            enemy.setDy(random.nextFloat() * 2 - 1);
        }

        // Move enemy
        float x = enemy.getX();
        float y = enemy.getY();

        x += enemy.getDx() * 50 * dt;
        y += enemy.getDy() * 50 * dt;

        // Screen wrapping
        if (x > gameData.getDisplayWidth()) x = 0;
        if (x < 0) x = gameData.getDisplayWidth();
        if (y > gameData.getDisplayHeight()) y = 0;
        if (y < 0) y = gameData.getDisplayHeight();

        // Update enemy position
        enemy.setX(x);
        enemy.setY(y);
        enemy.setRotation(Math.toDegrees(Math.atan2(enemy.getDy(), enemy.getDx())));
    }

    private void shoot(Entity enemy, GameData gameData, World world) {
        Collection<BulletSPI> bulletServices = ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).toList();

        for (BulletSPI bulletService : bulletServices) {
            Entity bullet = bulletService.createBullet(enemy, gameData);
            world.addEntity(bullet);
        }
    }
}