// Collision/src/main/java/module-info.java
module Collision {
    requires Common;
    exports dk.sdu.cbse.collision;

    provides dk.sdu.cbse.common.services.IPostEntityProcessingService
            with dk.sdu.cbse.collision.CollisionDetector;
}