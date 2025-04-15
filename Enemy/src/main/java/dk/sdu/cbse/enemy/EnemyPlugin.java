package dk.sdu.cbse.enemy;


import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;

import java.util.Random;

public class EnemyPlugin implements IGamePluginService {
    private Entity enemy;
    private final Random random = new Random();

    @Override
    public void start(GameData gameData, World world) {
        enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
    }

    private Entity createEnemyShip(GameData gameData) {
        Entity enemyShip = new Entity();
        enemyShip.setType("Enemy");
        enemyShip.setX(random.nextInt(gameData.getDisplayWidth()));
        enemyShip.setY(random.nextInt(gameData.getDisplayHeight()));
        enemyShip.setRadius(10);
        enemyShip.setLife(2);
        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
    }
}