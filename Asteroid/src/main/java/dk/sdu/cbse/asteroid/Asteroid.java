package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.Entity;

public class Asteroid extends Entity {
    private final int size; // 1=small, 2=medium, 3=large

    public Asteroid(int size) {
        setType("Asteroid");
        this.size = size;
        setLife(size);
        setRadius(size * 5); // 5, 10, or 15 depending on size
    }

    public int getSize() {
        return size;
    }
}