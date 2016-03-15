package tbs.bassjump.objects;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import tbs.bassjump.Game;
import tbs.bassjump.GameValues;
import tbs.bassjump.Utility;

public class Particle {
    public static final int STATE_ALIVE = 0; // particle is alive
    public static final int STATE_DEAD = 1; // particle is dead

    public static final int DEFAULT_LIFETIME = 500; // play with this
    public static final int MAX_DIMENSION = 5; // the maximum width or height
    public static final int MAX_SPEED = 10; // maximum speed (per update)
    public static Sprite particle;
    float a;
    private int state; // particle is alive or dead
    private float width; // width of the particle
    private float height; // height of the particle
    private float x, y; // horizontal and vertical position
    private double xv, yv; // vertical and horizontal velocity
    private int age; // current age of the particle
    private long setUpTime;

    public Particle() {
        //todo paint.setStrokeWidth(GameValues.STROKE_WIDTH / 3);
    }

    public void setup(int x, int y, boolean right) {
        this.x = x;
        this.y = y;
        this.state = Particle.STATE_ALIVE;
        this.width = Utility.randFloat(GameValues.SPLASH_MIN_SCALE,
                GameValues.SPLASH_MAX_SCALE);
        this.height = this.width;
        setUpTime = System.currentTimeMillis();
        this.age = 0;
        this.xv = (double) (Utility.randFloat(0, GameValues.SPEED_FACTOR));
        if (!right)
            this.xv *= -1;

        this.yv = (Utility.randInt(-GameValues.SPEED_FACTOR, GameValues.SPEED_FACTOR));
        // smoothing out the diagonal speed
        xv *= 0.85;
        yv *= 0.85;

        //Todo might have to change the a values
        a = 1;
    }

    public void update() {
        if (this.state != STATE_DEAD) {
            this.x += this.xv;
            if (xv > 0) { // GOING RIGHT
                if (x + width >= Game.w - GameValues.PLATFORM_WIDTH) {
                    xv *= -1;
                }
            } else { // GOING LEFT
                if (x <= GameValues.PLATFORM_WIDTH) {
                    xv *= -1;
                }
            }
            this.y += this.yv;
            this.yv += 2;

            a = (System.currentTimeMillis() - setUpTime) / 256f;

            if ((System.currentTimeMillis() - setUpTime) > DEFAULT_LIFETIME) {
                a = 0;
                state = STATE_DEAD;
            }
        }
    }

    public void draw(SpriteBatch canvas) {
        if (this.state == STATE_ALIVE) {
            particle.setSize(width, height);
            particle.setPosition(x, y);
            particle.setAlpha(a / 255f);
            particle.draw(canvas);
        }
    }
}