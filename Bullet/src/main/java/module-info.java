module Bullet {
    requires Common;
    exports dk.sdu.cbse.bullet;

    provides dk.sdu.cbse.common.bullet.BulletSPI;

    provides dk.sdu.cbse.common.services.IGamePluginService
            with dk.sdu.cbse.bullet.BulletPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService
            with dk.sdu.cbse.bullet.BulletControlSystem;
}