module Asteroid {
    requires Common;
    exports dk.sdu.cbse.asteroid;
    provides dk.sdu.cbse.common.services.IGamePluginService
            with dk.sdu.cbse.asteroid.AsteroidPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService
            with dk.sdu.cbse.asteroid.AsteroidControlSystem;

    requires spring.beans;
    requires spring.context;

    opens dk.sdu.cbse.asteroid to spring.core;
}