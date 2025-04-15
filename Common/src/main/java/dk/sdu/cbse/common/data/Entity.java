package dk.sdu.cbse.common.data;

import java.util.UUID;

public class Entity {
    private final UUID ID = UUID.randomUUID();
    private float x, y;
    private float dx, dy;
    private float radius;
    private String type;
    private int life;

    public String getID() {
        return ID.toString();
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public double getRotation() {
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    public void setPolygonCoordinates(int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        // This method is a placeholder for setting polygon coordinates.
        // The actual implementation would depend on the specific requirements of the game engine.
    }

    public void setRotation(double rotation) {
        // This method is a placeholder for setting the rotation of the entity.
        // The actual implementation would depend on the specific requirements of the game engine.
    }
}