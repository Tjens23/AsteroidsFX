module Player {
    requires Common;
    requires spring.beans;
    requires spring.context;
    exports dk.sdu.cbse.player;

    // Open package for Spring reflection
    opens dk.sdu.cbse.player to spring.core;

    // Keep existing provides statements
    provides dk.sdu.cbse.common.services.IGamePluginService
            with dk.sdu.cbse.player.PlayerPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService
            with dk.sdu.cbse.player.PlayerControlSystem;
}