package dk.sdu.cbse.bullet;

public class Bullet {
    private double x, y;
    private double dx, dy;

    public Bullet(double x, double y, double dx, double dy) {
        this.x = x; this.y = y;
        this.dx = dx; this.dy = dy;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
