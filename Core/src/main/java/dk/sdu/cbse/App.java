package dk.sdu.cbse;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.cbse.player.PlayerPlugin;
import dk.sdu.cbse.player.PlayerProcessor;
import dk.sdu.cbse.enemy.EnemyPlugin;
import dk.sdu.cbse.enemy.EnemyProcessor;
import dk.sdu.cbse.asteroid.AsteroidPlugin;
import dk.sdu.cbse.asteroid.AsteroidProcessor;
import dk.sdu.cbse.collision.CollisionDetector;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        GameData gameData = new GameData();
        World world = new World();

        // Set up game data
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);

        // Load plugins
        List<IGamePluginService> plugins = new ArrayList<>();
        plugins.add(new PlayerPlugin());
        plugins.add(new EnemyPlugin());
        plugins.add(new AsteroidPlugin());

        // Start all plugins
        for (IGamePluginService plugin : plugins) {
            plugin.start(gameData, world);
        }

        // Set up processors
        List<IEntityProcessingService> processors = new ArrayList<>();
        processors.add(new PlayerProcessor());
        processors.add(new EnemyProcessor());
        processors.add(new AsteroidProcessor());

        // Set up post processors
        List<IPostEntityProcessingService> postProcessors = new ArrayList<>();
        postProcessors.add(new CollisionDetector());

        // Game loop (simplified)
        for (int i = 0; i < 100; i++) {  // Simulate 100 frames
            gameData.setDelta(0.016f);  // ~60 FPS

            // Process entities
            for (IEntityProcessingService processor : processors) {
                processor.process(gameData, world);
            }

            // Post-process entities (collision)
            for (IPostEntityProcessingService postProcessor : postProcessors) {
                postProcessor.process(gameData, world);
            }

            // Print state (for debug)
            System.out.println("Frame: " + i + ", Entities: " + world.getEntities().size());
        }
    }
}