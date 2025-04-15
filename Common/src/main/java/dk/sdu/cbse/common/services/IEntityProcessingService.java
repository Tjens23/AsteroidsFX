package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * Service for processing game entities
 *
 * @pre GameData and World objects must contain valid data
 * @post Entities in the world are updated according to their behavior
 */
public interface IEntityProcessingService {
    /**
     * Process entity behaviors (movement, shooting, etc.)
     *
     * @param gameData the shared game data
     * @param world the world containing entities
     * @pre gameData and world must be non-null
     * @post entities in world are updated according to their behavior
     */
    void process(GameData gameData, World world);
}