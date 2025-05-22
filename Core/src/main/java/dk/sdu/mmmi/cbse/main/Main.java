package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import dk.sdu.mmmi.cbse.core.plugin.PluginService;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class Main extends Application {
    private static Game game;
    private AnnotationConfigApplicationContext ctx;
    private final GameData gameData = new GameData();
    private final World world = new World();

    @Override
    public void start(Stage window) throws Exception {
        System.out.println("=====================================");
        System.out.println("Starting AsteroidsFX game with dynamic plugin loading...");
        System.out.println("=====================================");

        // Create plugin-modules directory if it doesn't exist
        Path pluginDir = Paths.get("plugins");
        if (!Files.exists(pluginDir)) {
            Files.createDirectories(pluginDir);
        }

        try {
            System.out.println("\nInitializing Spring context...");
            ctx = new AnnotationConfigApplicationContext(ModuleConfig.class);

            // Demonstrate the split package scenario with plugin services
            demonstratePluginServices();

            System.out.println("\nRegistered Spring beans:");
            for (String beanName : ctx.getBeanDefinitionNames()) {
                System.out.println(" - " + beanName);
            }

            game = ctx.getBean(Game.class);
            game.start(window);
            game.render();

            setGame(game);

        } catch (Exception e) {
            System.err.println("Error initializing application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Demonstrates the PluginService implementations from both Core and dynamically loaded plugins.
     * This method shows how the ModuleLayer API handles the split package scenario.
     */
    private void demonstratePluginServices() {
        System.out.println("\n=====================================");
        System.out.println("DEMONSTRATING PLUGIN SYSTEM AND SPLIT PACKAGE HANDLING");
        System.out.println("=====================================");
        
        try {
            for (IGamePluginService iGamePlugin : getPluginServices()) {
                System.out.println("Starting plugin: " + iGamePlugin.getClass().getSimpleName());
                iGamePlugin.start(gameData, world);
            }

            //print the loaded plugin services
            System.out.println("\nLoaded plugin services:");
            List<PluginService> pluginServices = getPluginServices().stream()
                    .filter(service -> service instanceof PluginService)
                    .map(service -> (PluginService) service)
                    .collect(toList());

            for (PluginService service : pluginServices) {
                System.out.println(" - " + service.getClass().getName());
                System.out.println("   - Name: " + service.getName());
                System.out.println("   - Description: " + service.getDescription());
                System.out.println("   - Module: " + service.getClass().getModule().getName());
            }

            
            System.out.println("\nDemonstration of split package handling complete!");
            System.out.println("=====================================\n");
        } catch (Exception e) {
            System.err.println("Error during plugin service demonstration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Game getGame() {
        return game;
    }

    public static Game setGame(Game g) {
        game = g;
        return game;
    }
    private Collection<? extends IGamePluginService> getPluginServices() {
        return ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
