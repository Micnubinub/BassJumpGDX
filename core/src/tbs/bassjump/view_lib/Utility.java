package tbs.bassjump.view_lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import java.util.Random;

/**
 * Created by Riley on 6/05/15.
 */
public class Utility {
    public static final Random rand = new Random();
    public static final GlyphLayout layout = new GlyphLayout();
    private static final int[] ints = new int[2];
    private static boolean isFontInit;
    private static BitmapFont font;

    public static int getRandom(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static float randFloat(int minX, int maxX) {
        return rand.nextFloat() * (maxX - minX) + minX;
    }


    public static BitmapFont getFont() {
        if (!isFontInit) {
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
    }

    public static int[] getAnglePos(float angle, float distFromCenter, int x, int y) {
        angle = (float) Math.toRadians(angle + 270);

        ints[0] = (int) ((distFromCenter) * Math.sin(angle + Math.PI) + x);
        ints[1] = (int) ((distFromCenter) * Math.cos(angle + Math.PI) + y);
        return ints;
    }

    public static Sprite getResizedBitmap(Sprite sprite, int newHeight, int newWidth) {
        sprite.setSize(newWidth, newHeight);
        return sprite;
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
        final BitmapFont font = getFont();
        if (text == null || text.length() < 1 || font == null || batch == null)
            return;


        font.getData().setScale(scale);

        layout.setText(font, text);
        final float textWidth = layout.width;
        final float left = x - (textWidth / 2);
        final float textHeight = font.getLineHeight();
        font.setColor(color);
        font.draw(batch, text, left, y + (textHeight / 2));
    }

    public static void print(String str) {
        System.out.println(str);
    }

    public static void drawCenteredText(Batch batch, Color color, String text, float x, float y, float scale) {
        final BitmapFont font = getFont();
        if (text == null || text.length() < 1 || font == null || batch == null)
            return;
        print("sraw");

        font.getData().setScale(scale);

        layout.setText(font, text);
        final float textWidth = layout.width;
        final float left = x - (textWidth / 2);
        final float textHeight = font.getLineHeight();
        font.setColor(color);
        if (!batch.isDrawing())
            batch.begin();
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

    public static void clearScreen() {
        Gdx.gl.glClearColor(0.4f, 0.4f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
    }

    public static void drawCenteredText(SpriteBatch batch, String text, Color color, float scale, float x, float y) {
        final BitmapFont font = getFont();
        if (text == null || text.length() < 1 || font == null || batch == null)
            return;


        font.setColor(color);
        font.getData().setScale(scale);

        layout.setText(font, text);
        final float w = layout.width;
        final float h = layout.height;

        x = x - (w / 2);
        y = y + (h / 2);

        if (!batch.isDrawing())
            batch.begin();
        font.draw(batch, text, x, y);
    }

    public static void drawCenterLeftText(SpriteBatch batch, String text, Color color, float scale, float x, float y) {
        final BitmapFont font = getFont();
        if (text == null || text.length() < 1 || font == null || batch == null)
            return;

        font.setColor(color);
        font.getData().setScale(scale);

        layout.setText(font, text);
        final float w = layout.width;
        final float h = layout.height;

        y = y + (h / 2);

        if (!batch.isDrawing())
            batch.begin();
        font.draw(batch, text, x, y);
    }


    public static ShapeRenderer initShapeRenderer(SpriteBatch batch, ShapeRenderer renderer, Rectangle scissors, Rectangle clipBounds) {
        if (batch.isDrawing())
            try {
                try {
                    batch.end();
                } catch (Exception e) {
                }
                try {
                    ScissorStack.popScissors();
                } catch (Exception e) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        if (!renderer.isDrawing()) {
            try {
                ScissorStack.calculateScissors(HUDManager.camera, renderer.getTransformMatrix(), clipBounds, scissors);
                ScissorStack.pushScissors(scissors);
                renderer.begin(ShapeRenderer.ShapeType.Filled);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                renderer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ScissorStack.popScissors();
            } catch (Exception e) {
            }
        }

        return renderer;
    }


    public static SpriteBatch initSpriteBatch(SpriteBatch batch, ShapeRenderer renderer, Rectangle scissors, Rectangle clipBounds) {
        if (renderer.isDrawing())
            try {
                try {
                    renderer.end();
                } catch (Exception e) {
                }
                try {
                    ScissorStack.popScissors();
                } catch (Exception e) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        if (!batch.isDrawing()) {
            try {
                ScissorStack.calculateScissors(HUDManager.camera, batch.getTransformMatrix(), clipBounds, scissors);
                ScissorStack.pushScissors(scissors);
                batch.begin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                batch.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ScissorStack.popScissors();
            } catch (Exception e) {
            }
        }

        return batch;
    }

    public static void drawBottomLeftText(SpriteBatch batch, String text, Color color, float scale, float x, float y) {
        if (text == null || text.length() < 1 || font == null || batch == null)
            return;

        if (!batch.isDrawing())
            batch.begin();

        final BitmapFont font = getFont();
        font.setColor(color);
        font.getData().setScale(scale);

        layout.setText(font, text);
        final float h = layout.height;

        font.setColor(color);
        font.draw(batch, text, x, y + h);
    }

    public static void drawWithClippping(OrthographicCamera camera, SpriteBatch batch) {
        Rectangle scissors = new Rectangle();
//        Rectangle clipBounds = new Rectangle(x, y, width, height);

//        ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), clipBounds, scissors);
        ScissorStack.pushScissors(scissors);


        batch.flush();
        ScissorStack.popScissors();
    }
}
