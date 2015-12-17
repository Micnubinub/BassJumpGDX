package tbs.bassjump.objects;


import tbs.bassjump.Game;
import tbs.bassjump.utility.GameObject;

public class AnimCircle extends GameObject {

    // EXTRA
    public boolean active;
    public int a; // ALPHA
    public int scaleUp;
    private boolean special;

    public void update() {
        if (active) {
            this.scale += ((Game.w / (38 * 16f)) * Game.delta);
            this.a -= ((10 / 16f) * Game.delta);
            if (a <= 0) {
                active = false;
            }
        }
    }

    public void activate(int scale, int x, int y, int a, boolean special) {
        this.scale = scale;
        this.xPos = x;
        this.yPos = y;
        this.a = a;
        this.active = true;
        this.special = special;
    }
}
