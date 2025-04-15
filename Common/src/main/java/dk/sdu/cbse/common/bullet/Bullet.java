package dk.sdu.cbse.common.bullet;

import dk.sdu.cbse.common.data.Entity;

public class Bullet extends Entity {
    private Entity owner;

    public Bullet() {
        setType("Bullet");
    }

    public Entity getOwner() {
        return owner;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }
}