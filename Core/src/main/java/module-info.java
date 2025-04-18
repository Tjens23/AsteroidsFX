module Core {
    exports dk.sdu.cbse;
    requires Common;
    requires Player;
    requires Enemy;
    requires Asteroid;
    requires Collision;
    requires Bullet;
    uses dk.sdu.cbse.common.services.IGamePluginService;
    uses dk.sdu.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.cbse.common.services.IPostEntityProcessingService;

    requires spring.context;
    requires spring.beans;
    requires spring.core;
    opens dk.sdu.cbse to spring.core;
}