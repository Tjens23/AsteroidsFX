/**
 * Proposed interface for Life System
 */
package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

/**
 * Service for handling entity life management
 *
 * @pre Entity must be registered with the game world
 * @post Entity life state is correctly maintained
 */
public interface ILifeSystemService {
    /**
     * Damages an entity by reducing its life
     *
     * @param entity the entity to damage
     * @param amount the amount of damage to apply
     * @return true if the entity survived, false if destroyed
     * @pre entity must be non-null and have valid life value
     * @post entity's life is reduced by amount
     */
    boolean damageEntity(Entity entity, int amount);

    /**
     * Respawns an entity in the game world
     *
     * @param entityType the type of entity to respawn
     * @param gameData game data for positioning
     * @param world the world to add entity to
     * @pre entityType must be a valid type, gameData and world must be non-null
     * @post new entity of specified type is added to world
     */
    void respawnEntity(String entityType, GameData gameData, World world);
}