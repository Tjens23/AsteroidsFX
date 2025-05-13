package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.services.IEntityCircleCollision;
import dk.sdu.mmmi.cbse.common.services.IEntityHealthPoints;

public class Enemy extends Entity implements IEntityCircleCollision, IEntityHealthPoints {
    private double boundingCircleRadius;
    private int healthPoints;

    public Enemy() {
        setEntityType("ENEMY");
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
}
