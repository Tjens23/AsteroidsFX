module Enemy {
    uses dk.sdu.cbse.common.bullet.BulletSPI;
    exports dk.sdu.cbse.enemy;
    requires Bullet;
    requires Common;
    provides dk.sdu.cbse.common.services.IGamePluginService
            with dk.sdu.cbse.enemy.EnemyPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService
            with dk.sdu.cbse.enemy.EnemyControlSystem;
}