package dk.sdu.cbse.bullet;

import dk.sdu.cbse.common.bullet.Bullet;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import org.springframework.stereotype.Component;

@Component
public class BulletPlugin implements IGamePluginService {

    private Entity bullet;

    @Override
    public void start(GameData gameData, World world) {
        // Nothing to initialize
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Create a copy to avoid ConcurrentModificationException
        Entity[] entities = world.getEntities().toArray(new Entity[0]);
        for (Entity e : entities) {
            // Use instanceof instead of class comparison
            if (e instanceof Bullet) {
                world.removeEntity(e);
            }
        }
    }
}