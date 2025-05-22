package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyPlugin implements IGamePluginService {

    private final List<Entity> enemies;
    private final Random random = new Random();

    public EnemyPlugin() {
        enemies = new ArrayList<>();
    }

    @Override
    public void start(GameData gameData, World world) {
        // Create multiple enemies
        for (int i = 0; i < 5; i++) {
            Entity enemy = createEnemyShip(gameData);
            enemies.add(enemy);
            world.addEntity(enemy);
        }
    }

    private Entity createEnemyShip(GameData gameData) {
        Enemy enemyShip = new Enemy();
        enemyShip.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);

        // Randomize positions to avoid enemies spawning on top of each other
        enemyShip.setX(random.nextDouble() * gameData.getDisplayWidth());
        enemyShip.setY(random.nextDouble() * gameData.getDisplayHeight());

        enemyShip.setHealthPoints(3);
        enemyShip.setBoundingCircleRadius(5);

        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity enemy : enemies) {
            world.removeEntity(enemy);
        }
        enemies.clear();
    }
}