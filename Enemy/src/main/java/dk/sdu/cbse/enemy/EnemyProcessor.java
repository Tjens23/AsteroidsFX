package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import java.util.Random;

public class EnemyProcessor implements IEntityProcessingService {
    private final Random random = new Random();
    private int shootCooldown = 0;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities()) {
            moveRandomly(enemy, gameData);
            shootRandomly(enemy, gameData, world);
        }
    }

    private void moveRandomly(Entity enemy, GameData gameData) {
        if (random.nextInt(100) < 5) {
            enemy.setDx((random.nextFloat() - 0.5f) * 5);
            enemy.setDy((random.nextFloat() - 0.5f) * 5);
        }

        float x = enemy.getX() + enemy.getDx() * gameData.getDelta();
        float y = enemy.getY() + enemy.getDy() * gameData.getDelta();

        x = wrapAround(x, 0, gameData.getDisplayWidth());
        y = wrapAround(y, 0, gameData.getDisplayHeight());

        enemy.setX(x);
        enemy.setY(y);
    }

    private void shootRandomly(Entity enemy, GameData gameData, World world) {
        if (shootCooldown <= 0 && random.nextInt(100) < 10) {
            shootCooldown = 50;
        } else {
            shootCooldown--;
        }
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