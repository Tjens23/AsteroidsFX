package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.services.IEntityCircleCollision;
import dk.sdu.mmmi.cbse.common.services.IEntityHealthPoints;

/**
 *
 * @author Emil
 */
public class Player extends Entity implements IEntityCircleCollision, IEntityHealthPoints {
    private double boundingCircleRadius;
    private int healthPoints;

    public Player() {
        setEntityType("PLAYER");
    }

    @Override
    public double getBoundingCircleRadius() {
        return this.boundingCircleRadius;
    }

    public void setBoundingCircleRadius(double newBoundingCircleRadius) {
        this.boundingCircleRadius = newBoundingCircleRadius;
    }

    @Override
    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int newHealthPoints) {
        this.healthPoints = newHealthPoints;
    }

    @Override
    public void takeDamage(int damagePoints) {
        this.healthPoints -= damagePoints;
        if (this.healthPoints <= 0) {
            this.setDestroyed(true);
        }
    }

    public boolean isDestroyed() {
        return super.isDestroyed;
    }
}
