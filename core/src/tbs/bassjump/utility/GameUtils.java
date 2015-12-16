package tbs.bassjump.utility;


import com.badlogic.gdx.math.Matrix4;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

import tbs.bassjump.Game;
import tbs.bassjump.Utility;
import tbs.bassjump.objects.Player;
import tbs.bassjump.ui.ShapeView;


public class GameUtils {
    public static final String EQUIPPED_SONG = "EQUIPPED_SONG";
    public static final String EQUIPPED_SHAPE = "EQUIPPED_SHAPE";
    public static final String EQUIPPED_COLORS = "EQUIPPED_COLORS";
    public static final String BOUGHT_SONGS = "BOUGHT_SONGS";
    public static final String BOUGHT_COLORS = "BOUGHT_COLORS";
    public static final String BOUGHT_SHAPES = "BOUGHT_SHAPES";
    public static final String COINS = "COINS";
    public static final int SONG_PRICE = 500;
    public static final int COLOR_PRICE = 100;
    public static final String SEP = "//,/,//";

    public static final String COLOR_RED_DARK = "COLOR_RED_DARK";
    public static final String COLOR_PINK_DARK = "COLOR_PINK_DARK";
    public static final String COLOR_BLUE_DARK = "COLOR_BLUE_DARK";
    public static final String COLOR_INDIGO_DARK = "COLOR_INDIGO_DARK";
    public static final String COLOR_GREEN_DARK = "COLOR_GREEN_DARK";
    public static final String COLOR_YELLOW_DARK = "COLOR_YELLOW_DARK";
    public static final String COLOR_ORANGE_DARK = "COLOR_ORANGE_DARK";
    public static final String COLOR_PURPLE_DARK = "COLOR_PURPLE_DARK";

    public static final String COLOR_RED_LIGHT = "COLOR_RED_LIGHT";
    public static final String COLOR_PINK_LIGHT = "COLOR_PINK_LIGHT";
    public static final String COLOR_BLUE_LIGHT = "COLOR_BLUE_LIGHT";
    public static final String COLOR_INDIGO_LIGHT = "COLOR_INDIGO_LIGHT";
    public static final String COLOR_GREEN_LIGHT = "COLOR_GREEN_LIGHT";
    public static final String COLOR_YELLOW_LIGHT = "COLOR_YELLOW_LIGHT";
    public static final String COLOR_ORANGE_LIGHT = "COLOR_ORANGE_LIGHT";
    public static final String COLOR_PURPLE_LIGHT = "COLOR_PURPLE_LIGHT";

    public static final String COLOR_RED = "COLOR_RED";
    public static final String COLOR_PINK = "COLOR_PINK";
    public static final String COLOR_BLUE = "COLOR_BLUE";
    public static final String COLOR_INDIGO = "COLOR_INDIGO";
    public static final String COLOR_GREEN = "COLOR_GREEN";
    public static final String COLOR_YELLOW = "COLOR_YELLOW";
    public static final String COLOR_ORANGE = "COLOR_ORANGE";
    public static final String COLOR_PURPLE = "COLOR_PURPLE";
    public static final String COLOR_WHITE = "COLOR_WHITE";
    public static final String COLOR_BLACK = "COLOR_BLACK";
    public static final String COLOR_METALLIC = "COLOR_METALLIC";
    public static final String COLOR_INCOG = "COLOR_INCOG";
    public static final String COLOR_TWENTY = "COLOR_TWENTY";
    public static final String COLOR_CHOC = "COLOR_CHOC";
    public static final String COLOR_LIME = "COLOR_LIME";

    public static final String SHAPE_RECTANGLE = "SHAPE_RECTANGLE";
    public static final String SHAPE_TRIANGLE = "SHAPE_TRIANGLE";
    public static final String SHAPE_CIRCLE = "SHAPE_CIRCLE";
    public static final String SHAPE_PENTAGON = "SHAPE_PENTAGON";
    public static final String SHAPE_HEXAGON = "SHAPE_HEXAGON";
    public static final String SHAPE_SHURIKEN_STAR = "SHAPE_SHURIKEN_STAR";
    public static final String SHAPE_PENTAGON_STAR = "SHAPE_PENTAGON_STAR";
    public static final String CHECKOUT_OUR_OTHER_APPS = "CHECKOUT_OUR_OTHER_APPS";

    private static final Matrix4 matrix = new Matrix4();
    // RANDOM
    public static Random rand;
    public static boolean scanningForMusic;

    private static Thread songRefreshThread;

    public static void setupRandom() {
        rand = new Random();
    }

    public static boolean isScanningForMusic() {
        return scanningForMusic;
    }


    // Log Message
    public static void log(String message) {
        System.out.println(message);
    }

    // Random in Range
    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static float randFloat(int minX, int maxX) {
        return rand.nextFloat() * (maxX - minX) + minX;
    }

    public static void addGameColors() {
        final ArrayList<StoreItem> colors = GameUtils.getEquippedColorStoreItems();
        Utility.print("addingColors " + colors.toString());
        Game.colors = new int[colors.size()];
        for (int i = 0; i < colors.size(); i++) {
            Game.colors[i] = GameUtils.getColor(colors.get(i).tag);
        }

    }

    public static int generateRange(int num) {
        return GameUtils.randInt(-num / 3, num / 3);
    }

    // Stop Thread
    public static void stopThread(Thread thread) {
        if (thread == null)
            return;

        boolean retry = true;
        while (retry) {
            try {
                thread.interrupt();
                thread.join();
                retry = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void saveCoins(int coins) {
        Utility.saveInt(COINS, coins);
    }

    public static int getCoins() {
        return Utility.getInt(COINS);
    }

    public static ArrayList<StoreItem> getColorStoreItems() {

        final String boughtColors = getBoughtColors();
        final String equippedColors = getEquippedColors();
        final String[] colors = {COLOR_WHITE, COLOR_RED_LIGHT, COLOR_RED,
                COLOR_RED_DARK, COLOR_PINK_LIGHT, COLOR_PINK, COLOR_PINK_DARK,
                COLOR_BLUE_LIGHT, COLOR_BLUE, COLOR_BLUE_DARK,
                COLOR_INDIGO_LIGHT, COLOR_INDIGO, COLOR_INDIGO_DARK,
                COLOR_GREEN_LIGHT, COLOR_GREEN, COLOR_GREEN_DARK, COLOR_LIME,
                COLOR_YELLOW_LIGHT, COLOR_YELLOW, COLOR_YELLOW_DARK,
                COLOR_ORANGE_LIGHT, COLOR_ORANGE, COLOR_ORANGE_DARK,
                COLOR_PURPLE_LIGHT, COLOR_PURPLE, COLOR_PURPLE_DARK,
                COLOR_TWENTY, COLOR_CHOC, COLOR_INCOG, COLOR_METALLIC,
                COLOR_BLACK};
        final ArrayList<StoreItem> items = new ArrayList<StoreItem>(colors.length);
        for (String color : colors) {
            final StoreItem storeItem = getColorStoreItem(boughtColors, color);
            storeItem.equipped = equippedColors.contains(storeItem.tag);
            items.add(storeItem);
        }

        return items;
    }

    public static ArrayList<StoreItem> getEquippedColorStoreItems() {
        final String[] colors = GameUtils.getEquippedColors().split(SEP);
        final ArrayList<StoreItem> items = new ArrayList<StoreItem>(colors.length);
        for (String color : colors) {
            items.add(getColorStoreItem("", color));
        }
        return items;
    }

    public static ArrayList<StoreItem> getShapeStoreItems() {
        final String boughtShapes = getBoughtShapes();
        final String[] shapes = {SHAPE_RECTANGLE, SHAPE_TRIANGLE,
                SHAPE_CIRCLE, SHAPE_PENTAGON, SHAPE_HEXAGON, SHAPE_PENTAGON_STAR, SHAPE_SHURIKEN_STAR};
        final ArrayList<StoreItem> items = new ArrayList<StoreItem>(shapes.length);
        for (String shape : shapes) {
            items.add(getShapeStoreItem(boughtShapes, shape));
        }

        return items;
    }

    public static int getColor(String tag) {
        int color = 0xff292929;

        if (tag.equals(COLOR_RED_LIGHT))
            color = 0xffe84e40;
        else if (tag.equals(COLOR_RED))
            color = 0xffe51c23;
        else if (tag.equals(COLOR_RED_DARK))
            color = 0xffd01716;
        else if (tag.equals(COLOR_PINK_LIGHT))
            color = 0xfff06292;
        else if (tag.equals(COLOR_PINK))
            color = 0xffe91e63;
        else if (tag.equals(COLOR_PINK_DARK))
            color = 0xffc2185b;
        else if (tag.equals(COLOR_BLUE_LIGHT))
            color = 0xff738ffe;
        else if (tag.equals(COLOR_BLUE))
            color = 0xff5677fc;
        else if (tag.equals(COLOR_BLUE_DARK))
            color = 0xff455ede;
        else if (tag.equals(COLOR_INDIGO_LIGHT))
            color = 0xff5c6bc0;
        else if (tag.equals(COLOR_INDIGO))
            color = 0xff3f51b5;
        else if (tag.equals(COLOR_INDIGO_DARK))
            color = 0xff303f9f;
        else if (tag.equals(COLOR_GREEN_LIGHT))
            color = 0xff42bd41;
        else if (tag.equals(COLOR_GREEN))
            color = 0xff259b24;
        else if (tag.equals(COLOR_GREEN_DARK))
            color = 0xff0a7e07;
        else if (tag.equals(COLOR_LIME))
            color = 0xff32cd32;
        else if (tag.equals(COLOR_YELLOW_LIGHT))
            color = 0xfffff176;
        else if (tag.equals(COLOR_YELLOW))
            color = 0xffffeb3b;
        else if (tag.equals(COLOR_YELLOW_DARK))
            color = 0xfffdd835;
        else if (tag.equals(COLOR_ORANGE_LIGHT))
            color = 0xffffa726;
        else if (tag.equals(COLOR_ORANGE))
            color = 0xfffb8c00;
        else if (tag.equals(COLOR_ORANGE_DARK))
            color = 0xffe65100;
        else if (tag.equals(COLOR_PURPLE_LIGHT))
            color = 0xffab47bc;
        else if (tag.equals(COLOR_PURPLE))
            color = 0xff9c27b0;
        else if (tag.equals(COLOR_PURPLE_DARK))
            color = 0xff7b1fa2;
        else if (tag.equals(COLOR_BLACK))
            color = 0xff000000;
        else if (tag.equals(COLOR_METALLIC))
            color = 0xff535353;
        else if (tag.equals(COLOR_INCOG))
            color = 0xff292929;
        else if (tag.equals(COLOR_TWENTY))
            color = 0xff69661d;
        else if (tag.equals(COLOR_CHOC))
            color = 0xff53461a;
        else if (tag.equals(COLOR_WHITE))
            color = 0xffffffff;
//        else if (tag.equals(BACKGROUND_HUNNID))
//            color = 0xffdcdcdc;
//        else if (tag.equals(BACKGROUND_MIDNIGHT))
//            color = 0xff0000a0;
//        else if (tag.equals(BACKGROUND_ROAD))
//            color = 0xff525866;
//        else if (tag.equals(BACKGROUND_SKY))
//            color = 0xff3bb9ff;
        return color;
    }


    public static View getShape(String tag) {
        final View view = new ShapeView(context, getShapeType(tag));
        view.setLayoutParams(params);
        return view;
    }

    public static Player.PlayerShape getShapeType(String tag) {
        Player.PlayerShape shape = Player.PlayerShape.RECT;

        if (tag.equals(SHAPE_CIRCLE))
            shape = Player.PlayerShape.CIRCLE;
        else if (tag.equals(SHAPE_PENTAGON))
            shape = Player.PlayerShape.PENTAGON;
        else if (tag.equals(SHAPE_TRIANGLE))
            shape = Player.PlayerShape.TRIANGLE;
        else if (tag.equals(SHAPE_HEXAGON))
            shape = Player.PlayerShape.HEXAGON;
        else if (tag.equals(SHAPE_PENTAGON_STAR)) shape =
                Player.PlayerShape.PENTAGON_STAR;
        else if (tag.equals(SHAPE_SHURIKEN_STAR)) shape =
                Player.PlayerShape.SHURIKEN_STAR;
        return shape;
    }

    public static String getEquippedShape() {
        String out = Utility.getString(EQUIPPED_SHAPE);
        out = out == null ? "" : out;
        return out;
    }

    public static void removeEquippedColors(String tag) {
        String equippedColors = getEquippedColors();
        if (equippedColors.startsWith(tag)) {
            equippedColors.replace(tag + SEP, "");
        } else {
            equippedColors.replace(SEP + tag, "");
        }

        Utility.saveString(EQUIPPED_COLORS, equippedColors);
        addGameColors();
    }


    public static void equipShape(String tag) {
        Utility.saveString(EQUIPPED_SHAPE, tag);
        Player.setPlayerShape(getShapeType(tag));
    }

    public static String getBoughtShapes() {
        StringBuilder builder = new StringBuilder();
        builder.append(SHAPE_RECTANGLE);

        String out = Utility.getString(BOUGHT_SHAPES);
        out = out == null ? "" : out;

        if (out.length() < 2)
            return builder.toString();

        builder.append(SEP);
        builder.append(out);
        return builder.toString();
    }

    public static String getBoughtColors() {
        final StringBuilder builder = new StringBuilder();
        builder.append(COLOR_RED);

        String out = Utility.getString(BOUGHT_COLORS);
        out = (out == null) ? "" : out;

        if (out.length() < 2)
            return builder.toString();
        builder.append(SEP);
        builder.append(out);
        return builder.toString();
    }


    public static String getEquippedColors() {
        final StringBuilder builder = new StringBuilder();
        String out = Utility.getString(EQUIPPED_COLORS);
        out = out == null ? "" : out;

        if (out.length() < 4) {
            builder.append(COLOR_RED);
            return builder.toString();
        }

        return out;
    }

    public static void addBoughtShapes(String tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getBoughtShapes());
        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        Utility.saveString(BOUGHT_SHAPES, builder.toString());
        madePurchase();
    }

    public static void addBoughtColors(String tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getBoughtColors());
        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        Utility.saveString(BOUGHT_COLORS, builder.toString());
        addGameColors();
        madePurchase();
    }

    public static void addEquippedColors(String tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getEquippedColors());

        if (tag.equals(COLOR_RED)) {

        }

        if (builder.toString().contains(tag))
            return;

        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        Utility.saveString(EQUIPPED_COLORS, builder.toString());
        addGameColors();
        madePurchase();
    }


    public static void madePurchase() {
        MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQEw");
    }

    public static StoreItem getShapeStoreItem(String boughtShapes, String tag) {
        return new StoreItem(StoreItem.Type.SHAPE, tag, getShapeName(tag),
                "Shape", getShapePrice(tag), boughtShapes.contains(tag));
    }

    private static String getShapeName(String tag) {
        String shape = "Rectangle";

        if (tag.equals(SHAPE_CIRCLE))
            shape = "Circle";
        else if (tag.equals(SHAPE_HEXAGON))
            shape = "Hexagon";
        else if (tag.equals(SHAPE_TRIANGLE))
            shape = "Triangle";
        else if (tag.equals(SHAPE_PENTAGON))
            shape = "Pentagon";
        else if (tag.equals(SHAPE_SHURIKEN_STAR))
            shape = "Shuriken";
        else if (tag.equals(SHAPE_PENTAGON_STAR))
            shape = "Pentagram";

        return shape;
    }

    private static int getShapePrice(String tag) {
        int price = 0;
        if (tag.equals(SHAPE_TRIANGLE))
            price = 600;
        else if (tag.equals(SHAPE_CIRCLE))
            price = 1500;
        else if (tag.equals(SHAPE_PENTAGON))
            price = 2500;
        else if (tag.equals(SHAPE_HEXAGON))
            price = 10000;
        else if (tag.equals(SHAPE_PENTAGON_STAR))
            price = 12000;
        else if (tag.equals(SHAPE_SHURIKEN_STAR))
            price = 15000;
        return price;
    }

    public static StoreItem getColorStoreItem(String boughtColors, String tag) {
        if (tag.equals(COLOR_BLACK)) {
            return new StoreItem(StoreItem.Type.COLOR, tag, getColorName(tag),
                    "Color", 100000, boughtColors.contains(tag));
        } else if (tag.equals(COLOR_METALLIC)) {
            return new StoreItem(StoreItem.Type.COLOR, tag, getColorName(tag),
                    "Color", 50000, boughtColors.contains(tag));
        } else if (tag.equals(COLOR_INCOG)) {
            return new StoreItem(StoreItem.Type.COLOR, tag, getColorName(tag),
                    "Color", 10000, boughtColors.contains(tag));
        } else if (tag.equals(COLOR_TWENTY)) {
            return new StoreItem(StoreItem.Type.COLOR, tag, getColorName(tag),
                    "Color", 21, boughtColors.contains(tag));
        } else if (tag.equals(COLOR_CHOC)) {
            return new StoreItem(StoreItem.Type.COLOR, tag, getColorName(tag),
                    "Color", 5000, boughtColors.contains(tag));
        } else {
            return new StoreItem(StoreItem.Type.COLOR, tag, getColorName(tag),
                    "Color", COLOR_PRICE, boughtColors.contains(tag));
        }
    }

    private static String getColorName(String tag) {
        String color = "White";

        if (tag.equals(COLOR_RED_LIGHT))
            color = "Light red";
        else if (tag.equals(COLOR_RED))
            color = "Red";
        else if (tag.equals(COLOR_RED_DARK))
            color = "Dark red";
        else if (tag.equals(COLOR_PINK_LIGHT))
            color = "Light pink";
        else if (tag.equals(COLOR_PINK))
            color = "Pink";
        else if (tag.equals(COLOR_PINK_DARK))
            color = "Dark pink";
        else if (tag.equals(COLOR_BLUE_LIGHT))
            color = "Light Blue";
        else if (tag.equals(COLOR_BLUE))
            color = "Blue";
        else if (tag.equals(COLOR_BLUE_DARK))
            color = "Dark blue";
        else if (tag.equals(COLOR_INDIGO_LIGHT))
            color = "Light indigo";
        else if (tag.equals(COLOR_INDIGO))
            color = "Indigo";
        else if (tag.equals(COLOR_INDIGO_DARK))
            color = "Dark indigo";
        else if (tag.equals(COLOR_GREEN_LIGHT))
            color = "Light green";
        else if (tag.equals(COLOR_GREEN))
            color = "Green";
        else if (tag.equals(COLOR_GREEN_DARK))
            color = "Dark green";
        else if (tag.equals(COLOR_LIME))
            color = "Lime Green";
        else if (tag.equals(COLOR_YELLOW_LIGHT))
            color = "Light yellow";
        else if (tag.equals(COLOR_YELLOW))
            color = "Yellow";
        else if (tag.equals(COLOR_YELLOW_DARK))
            color = "Dark yellow";
        else if (tag.equals(COLOR_ORANGE_LIGHT))
            color = "Light orange";
        else if (tag.equals(COLOR_ORANGE))
            color = "Orange";
        else if (tag.equals(COLOR_ORANGE_DARK))
            color = "Dark orange";
        else if (tag.equals(COLOR_PURPLE_LIGHT))
            color = "Light Purple";
        else if (tag.equals(COLOR_PURPLE))
            color = "Purple";
        else if (tag.equals(COLOR_PURPLE_DARK))
            color = "Dark Purple";
        else if (tag.equals(COLOR_BLACK))
            color = "Vortex Black";
        else if (tag.equals(COLOR_METALLIC))
            color = "Tesla";
        else if (tag.equals(COLOR_INCOG))
            color = "Incog";
        else if (tag.equals(COLOR_TWENTY))
            color = "21";
        else if (tag.equals(COLOR_CHOC))
            color = "Chocolate";
        return color;
    }

    public static String formatNumber(int i) {
        return NumberFormat.getIntegerInstance().format(i);
    }

}
