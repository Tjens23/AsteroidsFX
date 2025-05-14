import dk.sdu.mmmi.cbse.common.services.IEntityCircleCollision;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

module Enemy {
    requires Common;
    requires CommonBullet;
    requires Player;
    uses dk.sdu.mmmi.cbse.playersystem.Player;
    uses dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
    provides IGamePluginService with dk.sdu.mmmi.cbse.enemy.EnemyPlugin;
    provides IEntityCircleCollision with dk.sdu.mmmi.cbse.enemy.Enemy;
    provides IEntityProcessingService with dk.sdu.mmmi.cbse.enemy.EnemyControlSystem;
    
    exports dk.sdu.mmmi.cbse.enemy;
}