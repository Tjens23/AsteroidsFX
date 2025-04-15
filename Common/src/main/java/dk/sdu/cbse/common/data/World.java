package dk.sdu.cbse.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {
    private final Map<String, Entity> entityMap = new ConcurrentHashMap<>();

    public void addEntity(Entity entity) {
        entityMap.put(entity.getID(), entity);
    }

    public void removeEntity(Entity entity) {
        entityMap.remove(entity.getID());
    }

    public void removeEntity(String entityID) {
        entityMap.remove(entityID);
    }

    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    public <E extends Entity> List<E> getEntities(Class<E> entityType) {
        List<E> entities = new ArrayList<>();
        for (Entity e : getEntities()) {
            if (entityType.isInstance(e)) {
                entities.add(entityType.cast(e));
            }
        }
        return entities;
    }
}
