package tbs.bassjump;


import com.badlogic.gdx.Gdx;

public class ScreenDimen {
    // VARIABLES
    public static int width;
    public static int height;

    public static void setup() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    public static int getCenterX() {
        return width / 2;
    }

    public static int getCenterY() {
        return height / 2;
    }

}
