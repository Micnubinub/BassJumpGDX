package tbs.bassjump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

import java.util.Random;

import tbs.bassjump.objects.Player;

/**
 * Created by Riley on 6/05/15.
 */
public class Utility {

    public static final String EQUIPPED_SHAPE = "EQUIPPED_SHAPE";
    public static final String EQUIPPED_COLOR = "EQUIPPED_COLOR";
    public static final String BOUGHT_COLORS = "BOUGHT_COLORS";
    public static final String BOUGHT_SHAPES = "BOUGHT_SHAPES";
    public static final String COINS = "COINS";
    public static final int COLOR_PRICE = 100;
    public static final String COLOR_PRICE_S = "100";
    public static final String SEP = "//,/,//";

    public static final String CHECKOUT_OUR_OTHER_APPS = "CHECKOUT_OUR_OTHER_APPS";

    public static final Random rand = new Random();
    public static final int[] colors = new int[]{0xff292929, 0xffe84e40, 0xffe51c23, 0xffd01716, 0xfff06292, 0xffe91e63, 0xffc2185b, 0xff738ffe, 0xff5677fc, 0xff455ede, 0xff5c6bc0, 0xff3f51b5, 0xff303f9f, 0xff42bd41, 0xff259b24, 0xff0a7e07, 0xff32cd32, 0xfffff176, 0xffffeb3b, 0xfffdd835, 0xffffa726, 0xfffb8c00, 0xffe65100, 0xffab47bc, 0xff9c27b0, 0xff7b1fa2, 0xff000000, 0xff535353, 0xff292929, 0xff69661d, 0xff53461a, 0xffffffff};
    public static final int[] shapePrices = new int[]{0, 600, 1500, 2500, 10000, 12000, 15000};
    public static final String[] shapePricesS = new String[]{"0", "600", "1500", "2500", "10000", "12000", "15000"};
    public static final String[] shapeNames = new String[]{"Rectangle", "Circle", "Hexagon", "Triangle", "Pentagon", "Shuriken", "Pentagram"};
    public static final String[] colorNames = new String[]{"White", "Light red", "Red", "Dark red", "Light pink", "Pink", "Dark pink", "Light Blue", "Blue", "Dark blue", "Light indigo", "Indigo", "Dark indigo", "Light green", "Green", "Dark green", "Lime Green", "Light yellow", "Yellow", "Dark yellow", "Light orange", "Orange", "Dark orange", "Light Purple", "Purple", "Dark Purple", "Vortex Black", "Tesla", "Incog", "21", "Chocolate"};
    public static final GlyphLayout glyphLayout = new GlyphLayout();
    private static final int[] ints = new int[2];
    private static final Color c = new Color();
    private static final float[] textSize = new float[2];
    private static final String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\nattribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\nattribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\nuniform mat4 u_projTrans;\n"
            + "varying vec4 v_color;\nvarying vec2 v_texCoords;\nvoid main() {\nv_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\nv_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n"
            + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n}";
    private static boolean isFontInit;
    private static BitmapFont font;

    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
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


    public static int generateRange(int num) {
        return Utility.randInt(-num / 3, num / 3);
    }

    public static void saveCoins(int coins) {
        saveInt(COINS, coins);
    }

    public static int getCoins() {
        int coins = 0;
        try {
            coins = getInt(COINS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coins;
    }

    public static int getEquippedShape() {
        return getInt(EQUIPPED_SHAPE);
    }

    public static void equipShape(int tag) {
        saveInt(EQUIPPED_SHAPE, tag);
        Player.setPlayerSprite();
    }


    public static String[] getBoughtShapes() {
        return getString(BOUGHT_SHAPES).split(SEP);
    }

    public static String[] getBoughtColors() {
        return getString(BOUGHT_COLORS).split(SEP);
    }

    public static void equipColor(int index) {
        saveInt(EQUIPPED_COLOR, index);
//        dispose(Game.shaderProgram);
//        Game.shaderProgram = getCarShaderProgram(light[index], dark[index]);
    }

    public static boolean contains(String[] array, String item) {
        for (String s : array) {
            if (s.equals(item))
                return true;
        }
        return false;
    }

    public static int getEquippedColor() {
        return getInt(EQUIPPED_COLOR);
    }

    public static void addBoughtShapes(int tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getBoughtShapes());
        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        saveString(BOUGHT_SHAPES, builder.toString());
        madePurchase();
    }

    public static void addBoughtColors(int tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getBoughtColors());
        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        saveString(BOUGHT_COLORS, builder.toString());
        madePurchase();
    }

    public static void madePurchase() {
//    todo    MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQEw");
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

        if (text == null || text.length() < 1) {
            return;
        }
//        font.getData().setScale(scale);
//        textToMeasure = "" + player.score;
//        glyphLayout.setText(font, textToMeasure);
//        color.set(0xFFFFFFFF);
        final BitmapFont font = getFont();
        font.getData().setScale(scale * 1.25f);

        glyphLayout.setText(font, text);
        final float textWidth = glyphLayout.width;
        final float left = x - (textWidth / 2);
        final float textHeight = font.getLineHeight();
        font.setColor(color);
        font.draw(batch, text, left, y + (textHeight / 2));
    }

    public static void drawLeftText(SpriteBatch batch, Color color, String text, float x, float y, float scale) {

        if (text == null || text.length() < 1) {
            return;
        }

        final BitmapFont font = getFont();
        font.getData().setScale(scale * 1.25f);
        font.setColor(color);
        font.draw(batch, text, x, y + font.getLineHeight());
    }

    public static boolean customBool(int i) {
        for (int ii = 0; ii < i; ii++) {
            if (!rand.nextBoolean()) {
                return false;
            }
        }
        return true;
    }

    public static void print(String s) {
        System.out.println(s);
    }

    public static void openLink(String link) {
        Gdx.net.openURI(link);
    }

    public static String formatNumber(int i) {
//  Todo      return NumberFormat.getIntegerInstance().format(i);
        return String.valueOf(i);
    }

    public static float[] measureText(String text) {
        if (text == null || text.length() < 1) {
            textSize[0] = 0;
            textSize[1] = 0;
        } else {
            final BitmapFont font = getFont();
            glyphLayout.setText(font, text);
            textSize[0] = glyphLayout.width;
            textSize[1] = glyphLayout.height;
        }
        return textSize;
    }

    public static float[] measureText(String text, float scale) {
        getFont().getData().setScale(scale);
        return measureText(text);
    }


    public static ShaderProgram getCircleViewShaderProgram(int color) {
        c.set(color);
        final String fragmentShader = String.format("#ifdef GL_ES\n"
                        + "#define LOWP lowp\n"
                        + "precision mediump float;\n"
                        + "#else\n"
                        + "#define LOWP \n"
                        + "#endif\n"
                        + "varying LOWP vec4 v_color;\n"
                        + "varying vec2 v_texCoords;\n"
                        + "uniform sampler2D u_texture;\n"
                        + "void main() {\n"
                        + "  vec4 v_c = v_color * texture2D(u_texture, v_texCoords);\n"
                        + "  if (v_c.a > 0.95) { \n"
                        + " if ((v_c.r + 0.3 > 0.0 && v_c.r - 0.3 < 0.0)"
                        + "&& (v_c.g + 0.3 > 0.0 && v_c.g - 0.3 < 0.0)"
                        + "&& (v_c.b + 0.3 > 0.0 && v_c.b - 0.3 < 0.0)){\n"
                        + "           v_c.r = %f;\n"
                        + "           v_c.g = %f;\n"
                        + "           v_c.b = %f;\n"
                        + "         } \n"
                        + "  } \n"
                        + "  gl_FragColor = v_c;\n"
                        + "}"
                //Todo all colors are argb instead of rgba, so this is special >> its equivalent to
                //todo : c.r, c.g, c.b, c2.r, c2.g, c2.b
                , c.r, c.g, c.b);

        final ShaderProgram p = new ShaderProgram(vertexShader, fragmentShader);
        if (!p.isCompiled()) {
            Gdx.app.error("getCarShaderProgram failed", p.getLog());
            dispose(p);
            return null;
        }
        return p;
    }

    public static ShaderProgram getBuyButtonShaderProgram(int color) {
        c.set(color);
        final String fragmentShader = String.format("#ifdef GL_ES\n"
                        + "#define LOWP lowp\n"
                        + "precision mediump float;\n"
                        + "#else\n"
                        + "#define LOWP \n"
                        + "#endif\n"
                        + "varying LOWP vec4 v_color;\n"
                        + "varying vec2 v_texCoords;\n"
                        + "uniform sampler2D u_texture;\n"
                        + "void main() {\n"
                        + "  vec4 v_c = v_color * texture2D(u_texture, v_texCoords);\n"
                        + "  if (v_c.a > 0.95) { \n"
                        + "           v_c.r = %f;\n"
                        + "           v_c.g = %f;\n"
                        + "           v_c.b = %f;\n"
                        + "   }\n"
                        + "  gl_FragColor = v_c;\n"
                        + "}"
                //Todo all colors are argb instead of rgba, so this is special >> its equivalent to
                //todo : c.r, c.g, c.b, c2.r, c2.g, c2.b
                , c.r, c.g, c.b);

        final ShaderProgram p = new ShaderProgram(vertexShader, fragmentShader);
        if (!p.isCompiled()) {
            Gdx.app.error("getCarShaderProgram failed", p.getLog());
            dispose(p);
            return null;
        }
        return p;
    }

    public static void dispose(Object o) {
        if (o != null && o instanceof Disposable) {
            try {
                ((Disposable) o).dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        else {
//            Gdx.app.error("Util: ", "Not dosposable");
//        }
    }

}
