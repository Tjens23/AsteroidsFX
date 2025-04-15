// Common/src/main/java/dk/sdu/cbse/common/data/GameData.java
package dk.sdu.cbse.common.data;

public class GameData {
    private float delta;
    private int displayWidth;
    private int displayHeight;
    private final GameKeys keys = new GameKeys();

    public float getDelta() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public GameKeys getKeys() {
        return keys;
    }
}