package dk.sdu.mmmi.cbse.common.services;

public interface IEntityHealthPoints {
    int getHealthPoints();
    void takeDamage(int damagePoints);
}
