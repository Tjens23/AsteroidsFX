package dk.sdu.cbse.enemy;

import dk.sdu.cbse.bullet.Bullet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySpaceship {
    private double x, y;
    private double dx, dy;
    private final List<Bullet> bullets = new ArrayList<>();
    private final Random random = new Random();
    private int shootCooldown = 0;

    public EnemySpaceship(double x, double y) {
        this.x = x;
        this.y = y;
        this.dx = getRandomSpeed();
        this.dy = getRandomSpeed();
    }

    private double getRandomSpeed() {
        return (random.nextDouble() - 0.5) * 5;

    }

    public void update() {
        x += dx;
        y += dy;

        // Change direction occasionally
        if (random.nextInt(100) < 5) {
            dx = getRandomSpeed();
            dy = getRandomSpeed();
        }

        // Shoot occasionally
        if (shootCooldown <= 0 && random.nextInt(100) < 10) {
            bullets.add(new Bullet(x, y, getRandomSpeed(), getRandomSpeed()));
            shootCooldown = 50;
        } else {
            shootCooldown--;
        }

        bullets.forEach(Bullet::update);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
