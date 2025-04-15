package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * Service for post-processing entities (typically for collision detection)
 *
 * @pre GameData and World objects contain entities that have been processed
 * @post Collision detection and resolution has been performed
 */
public interface IPostEntityProcessingService {
    /**
     * Performs post-processing of entities (like collision detection)
     *
     * @param gameData the shared game data
     * @param world the world containing processed entities
     * @pre gameData and world must be non-null, entities must be processed
     * @post collisions are detected and resolved
     */
    void process(GameData gameData, World world);
}