package dk.sdu.cbse;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Initialize Spring context
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);

        GameData gameData = new GameData();
        World world = new World();

        // Set up game data
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);

        // Get plugins through Spring
        List<IGamePluginService> plugins =
            context.getBeansOfType(IGamePluginService.class).values().stream().toList();

        // Start all plugins
        for (IGamePluginService plugin : plugins) {
            plugin.start(gameData, world);
        }

        // Get processors through Spring
        List<IEntityProcessingService> processors =
            context.getBeansOfType(IEntityProcessingService.class).values().stream().toList();

        // Get post processors through Spring
        List<IPostEntityProcessingService> postProcessors =
            context.getBeansOfType(IPostEntityProcessingService.class).values().stream().toList();

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

        // Close the Spring context
        context.close();
    }
}