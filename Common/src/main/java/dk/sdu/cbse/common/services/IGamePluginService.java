package dk.sdu.cbse.common.services;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * Service for game plugins that can start and stop game elements
 *
 * @pre GameData and World objects must be properly initialized
 * @post The plugin's game elements are added to or removed from the world
 */
public interface IGamePluginService {
    /**
     * Starts the plugin and adds its entities to the game world
     *
     * @param gameData the shared game data
     * @param world the world to add entities to
     * @pre gameData and world must be non-null
     * @post plugin's entities are added to the world
     */
    void start(GameData gameData, World world);

    /**
     * Stops the plugin and removes its entities from the world
     *
     * @param gameData the shared game data
     * @param world the world to remove entities from
     * @pre gameData and world must be non-null
     * @post plugin's entities are removed from the world
     */
    void stop(GameData gameData, World world);
}
