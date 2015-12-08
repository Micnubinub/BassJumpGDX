package tbs.bassjump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

/**
 * Created by Riley on 6/05/15.
 */
public class Utility {

    public static final Random rand = new Random();
    private static final int[] ints = new int[2];
    private static final GlyphLayout layout = new GlyphLayout();
    private static boolean isFontInit;
    private static BitmapFont font;

    public static int getRandom(int min, int max) {
        return rand.nextInt((max - min) < 0 ? 0 : (max - min) + 1) + min;
    }

    public static float randFloat(int minX, int maxX) {
        return rand.nextFloat() * (maxX - minX) + minX;
    }

    public static BitmapFont getFont() {
        if (!isFontInit || font == null) {
            font = new BitmapFont(Gdx.files.internal("font.fnt"));
            isFontInit = true;
        }
        return font;
    }

    public static float getScale(float textHeight) {
        return textHeight / 192f;
    }

    public static void disposeFont() {
        isFontInit = false;
        try {
            font.dispose();
        } catch (Exception e) {
        }
        font = null;
    }

    public static int[] getAnglePos(double angle, float distFromCenter, float x, float y) {
        ints[0] = (int) (Math.round(distFromCenter * Math.sin(angle)) + x);
        ints[1] = (int) (Math.round(distFromCenter * Math.cos(angle)) + y);
        return ints;
    }

    public static Preferences getPreferences() {
        return Gdx.app.getPreferences("prefs");
    }

    public static int getInt(String key) {
        return getPreferences().getInteger(key, 0);
    }

    public static void saveInt(String key, int value) {
        getPreferences().putInteger(key, value).flush();
    }

    public static String getString(String key) {
        return getPreferences().getString(key, "");
    }

    public static void saveString(String key, String value) {
        getPreferences().putString(key, value).flush();
    }

    public static void drawCenteredText(SpriteBatch batch, Color color, String text, float x, float y, float scale) {

//        font.getData().setScale(scale);
//        textToMeasure = "" + player.score;
//        glyphLayout.setText(font, textToMeasure);
//        color.set(0xFFFFFFFF);
        final BitmapFont font = getFont();
        font.getData().setScale(scale * 1.25f);

        layout.setText(font, text);
        final float textWidth = layout.width;
        final float left = x - (textWidth / 2);
        final float textHeight = font.getLineHeight();
        font.setColor(color);
        font.draw(batch, text, left, y + (textHeight / 2));
    }

    public static boolean customBool(int i) {
        for (int ii = 0; ii < i; ii++) {
            if (!rand.nextBoolean()) {
                return false;
            }
        }
        return true;
    }


}
