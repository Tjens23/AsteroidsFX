package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class EnemyPlugin implements IGamePluginService {
    private final Random random = new Random();

    @Override
    public void start(GameData gameData, World world) {
        // Create enemy ship
        Entity enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
    }

    private Entity createEnemyShip(GameData gameData) {
        Entity enemy = new Enemy();

        float x = random.nextFloat() * gameData.getDisplayWidth();
        float y = random.nextFloat() * gameData.getDisplayHeight();

        enemy.setX(x);
        enemy.setY(y);
        enemy.setDx(random.nextFloat() * 2 - 1);
        enemy.setDy(random.nextFloat() * 2 - 1);
        enemy.setRadius(10);
        enemy.setPolygonCoordinates(0, -10, 10, 10, -10, 10, 0, -10);

        return enemy;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Enemy) {
                world.removeEntity(entity);
            }
        }
    }
}