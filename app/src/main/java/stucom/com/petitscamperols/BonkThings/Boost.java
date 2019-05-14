package stucom.com.petitscamperols.BonkThings;

import stucom.com.petitscamperols.GameEngine;

public class Boost extends Character {

    public Boost(GameEngine gameEngine, int x, int y) {
        super(gameEngine, x, y);
    }

    private static final int[][] ANIMATIONS = new int[][]{
            new int[]{39}
    };

    @Override
    int[][] getAnimations() {
        return ANIMATIONS;
    }

    @Override
    void updatePhysics(int delta) {
    }

    @Override
    void updateCollisionRect() {
        collisionRect.set(x, y, x + 16, y + 16);
    }

    @Override
    public String toString() {
        return "Boost{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
