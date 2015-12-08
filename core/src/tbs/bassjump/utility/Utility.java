package tbs.bassjump.utility;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tbs.jumpsnew.Game;
import tbs.jumpsnew.MainActivity;
import tbs.jumpsnew.R;
import tbs.jumpsnew.fragments.MusicFragment;
import tbs.jumpsnew.managers.FileManager;
import tbs.jumpsnew.objects.Player;
import tbs.jumpsnew.ui.ColorView;
import tbs.jumpsnew.ui.ShapeView;

public class Utility {
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
    private static final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT);
    private static final Matrix matrix = new Matrix();
    // RANDOM
    public static Random rand;
    public static boolean scanningForMusic;
    private static ArrayList<StoreItem> songs;
    private static final Runnable songRefresher = new Runnable() {
        @Override
        public void run() {
            scanningForMusic = true;
            songs = new ArrayList<StoreItem>();
            songs.add(new StoreItem(StoreItem.Type.SONG, Uri.parse(
                    "android.resource://"
                            + Game.context.getApplicationInfo().packageName
                            + "/" + R.raw.song1).toString(), "Colossus", "Meizong",
                    0, true));

            final String boughtSongs = getBoughtSongs(Game.context);
            final String equippedSongs = getEquippedSongs(Game.context);
            final ArrayList<File> songFiles = FileManager.scanForMusic();
            for (int i = 0; i < songFiles.size(); i++) {
                final StoreItem storeItem = getSongStoreItem(boughtSongs, songFiles.get(i).getAbsolutePath());
                storeItem.equipped = equippedSongs.contains(storeItem.tag);
                if (MusicFragment.listView != null) {
                    MainActivity.getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (MusicFragment.adapter != null) {
                                songs.add(storeItem);
                                MusicFragment.adapter.notifyDataSetChanged();
                            }
                        }
                    });
                } else {
                    songs.add(storeItem);
                }
            }
            scanningForMusic = false;
        }
    };
    private static Thread songRefreshThread;

    public static void setupRandom() {
        rand = new Random();
    }

    public static boolean isScanningForMusic() {
        return scanningForMusic;
    }

    public static void showToast(String a, Context context) {
        Toast.makeText(context, a, Toast.LENGTH_SHORT).show();
    }

    public static void refreshSongs() {
        try {
            stopThread(songRefreshThread);
            if (songRefreshThread == null) {
                songRefreshThread = new Thread(songRefresher);
            }

            if (songRefreshThread.isAlive())
                return;


            songRefreshThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Log Message
    public static void log(String message) {
        Log.e(MainActivity.TAG, message);
    }

    // Random in Range
    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static float randFloat(int minX, int maxX) {
        return rand.nextFloat() * (maxX - minX) + minX;
    }

    public static void addGameColors() {
        final ArrayList<StoreItem> colors = Utility.getEquippedColorStoreItems(Game.context);
        Log.e("addingColors", colors.toString());
        Game.colors = new int[colors.size()];
        for (int i = 0; i < colors.size(); i++) {
            Game.colors[i] = Utility.getColor(colors.get(i).tag);
        }

    }

    public static int generateRange(int num) {
        return Utility.randInt(-num / 3, num / 3);
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

    // Resize Bitmap
    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.reset();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    // Load Bitmap from Location
    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
        }
        return bitmap;
    }

    public static void saveCoins(Context context, int coins) {
        getPrefs(context).put(COINS, String.valueOf(coins));
    }

    public static int getCoins(Context context) {
        int coins = 0;
        try {
            coins = Integer.parseInt(getPrefs(context).getString(COINS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coins;
    }

    public static ArrayList<StoreItem> getColorStoreItems(Context context) {

        final String boughtColors = getBoughtColors(context);
        final String equippedColors = getEquippedColors(context);
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

    public static ArrayList<StoreItem> getEquippedColorStoreItems(Context context) {
        final String[] colors = Utility.getEquippedColors(context).split(SEP);
        final ArrayList<StoreItem> items = new ArrayList<StoreItem>(colors.length);
        for (String color : colors) {
            items.add(getColorStoreItem("", color));
        }
        return items;
    }

    public static ArrayList<StoreItem> getShapeStoreItems(Context context) {
        final String boughtShapes = getBoughtShapes(context);
        final String[] shapes = {SHAPE_RECTANGLE, SHAPE_TRIANGLE,
                SHAPE_CIRCLE, SHAPE_PENTAGON, SHAPE_HEXAGON, SHAPE_PENTAGON_STAR, SHAPE_SHURIKEN_STAR};
        final ArrayList<StoreItem> items = new ArrayList<StoreItem>(shapes.length);
        for (String shape : shapes) {
            items.add(getShapeStoreItem(boughtShapes, shape));
        }

        return items;
    }

    public static ArrayList<StoreItem> getSongStoreItems() {
        return songs;
    }

    public static View getColor(Context context, String tag) {
        View view = new ColorView(context, getColor(tag));
        view.setLayoutParams(params);
        return view;
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


    public static View getShape(Context context, String tag) {
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

    public static String getEquippedShape(Context context) {
        String out = getPrefs(context).getString(EQUIPPED_SHAPE);
        out = out == null ? "" : out;
        return out;
    }

    public static String getEquippedSongs(Context context) {
        String out = getPrefs(context).getString(EQUIPPED_SONG);
        out = out == null ? "" : out;
        out = out.length() < 2 ? "android.resource://"
                + context.getApplicationInfo().packageName + "/raw/song1" : out;
        return out;
    }

    public static void removeEquippedColors(Context context, String tag) {
        String equippedColors = getEquippedColors(context);
        if (equippedColors.startsWith(tag)) {
            equippedColors.replace(tag + SEP, "");
        } else {
            equippedColors.replace(SEP + tag, "");
        }

        getPrefs(context).put(EQUIPPED_COLORS, equippedColors);
        addGameColors();
    }

    public static void removeEquippedSongs(Context context, String tag) {
        try {
            String equippedSongs = getEquippedSongs(context);
            if (equippedSongs.startsWith(tag)) {
                equippedSongs.replace(tag + SEP, "");
            } else {
                equippedSongs.replace(SEP + tag, "");
            }
            getPrefs(context).put(EQUIPPED_SONG, equippedSongs);
            equipSongs(context, equippedSongs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void equipShape(Context context, String tag) {
        getPrefs(context).put(EQUIPPED_SHAPE, tag);
        Player.setPlayerShape(getShapeType(tag));
    }

    public static void equipSongs(Context context, String tag) {
        getPrefs(context).put(EQUIPPED_SONG, tag);
        Game.songs = tag.split(SEP);
        Log.e("songs", Arrays.toString(Game.songs));
//        if (tag == null || tag.length() < 1)
//           // tbs.bassjump.reference.Game.playDefaultSong();
//        else {
        try {
            Game.playNextSong();
        } catch (Exception e) {
            // tbs.bassjump.reference.Game.playDefaultSong();
        }
        // }
    }

    public static String getBoughtShapes(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append(SHAPE_RECTANGLE);

        String out = getPrefs(context).getString(BOUGHT_SHAPES);
        out = out == null ? "" : out;

        if (out.length() < 2)
            return builder.toString();

        builder.append(SEP);
        builder.append(out);
        return builder.toString();
    }

    public static String getBoughtSongs(Context context) {
        final StringBuilder builder = new StringBuilder();
        builder.append("android.resource://"
                + context.getApplicationInfo().packageName + "/raw/song1");

        String out = getPrefs(context).getString(BOUGHT_SONGS);
        out = (out == null) ? "" : out;

        if (out.length() < 2)
            return builder.toString();

        builder.append(SEP);
        builder.append(out);
        return builder.toString();
    }


    public static String getBoughtColors(Context context) {
        final StringBuilder builder = new StringBuilder();
        builder.append(COLOR_RED);

        String out = getPrefs(context).getString(BOUGHT_COLORS);
        out = (out == null) ? "" : out;

        if (out.length() < 2)
            return builder.toString();
        builder.append(SEP);
        builder.append(out);
        return builder.toString();
    }


    public static String getEquippedColors(Context context) {
        final StringBuilder builder = new StringBuilder();
        String out = getPrefs(context).getString(EQUIPPED_COLORS);
        out = out == null ? "" : out;

        if (out.length() < 4) {
            builder.append(COLOR_RED);
            return builder.toString();
        }

        return out;
    }

    public static void addBoughtShapes(Context context, String tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getBoughtShapes(context));
        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        getPrefs(context).put(BOUGHT_SHAPES, builder.toString());
        madePurchase();
    }

    public static void addBoughtSongs(Context context, String tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getBoughtSongs(context));
        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        getPrefs(context).put(BOUGHT_SONGS, builder.toString());
        madePurchase();
    }

    public static void addBoughtColors(Context context, String tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getBoughtColors(context));
        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        getPrefs(context).put(BOUGHT_COLORS, builder.toString());
        addGameColors();
        madePurchase();
    }

    public static void addEquippedColors(final Context context, String tag) {
        final StringBuilder builder = new StringBuilder();
        builder.append(getEquippedColors(context));

        if (tag.equals(COLOR_RED)) {

        }

        if (builder.toString().contains(tag))
            return;

        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        getPrefs(context).put(EQUIPPED_COLORS, builder.toString());
        addGameColors();
        madePurchase();
    }

    public static void addEquippedSongs(Context context, String tag) {
        Log.e("addESongs", tag);
        final StringBuilder builder = new StringBuilder();
        builder.append(getEquippedSongs(context));

        if (builder.toString().contains(tag))
            return;

        if (builder.toString().length() > 1)
            builder.append(SEP);
        builder.append(tag);
        getPrefs(context).put(EQUIPPED_SONG, builder.toString());
        equipSongs(context, builder.toString());
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

    public static StoreItem getSongStoreItem(String boughtSongs, String song) {
        try {
            final String[] songDetails = getSongTitle(song).split(SEP);
            return new StoreItem(StoreItem.Type.SONG, song, songDetails[0],
                    songDetails[1], SONG_PRICE, boughtSongs.contains(song));
        } catch (ArrayIndexOutOfBoundsException e) {
            return new StoreItem(StoreItem.Type.SONG, song, "?", "?",
                    SONG_PRICE, boughtSongs.contains(song));
        } catch (Exception ex) {
            return new StoreItem(StoreItem.Type.SONG, song, "?", "?",
                    SONG_PRICE, boughtSongs.contains(song));
        }
    }

    public static String getSongTitle(String song) {
        try {
            if (song.contains("android.resource://"))
                return getRawFileSongName(song);

            final MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(song);

            String title = mmr
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (title == null || title.length() < 1 || title.equals("null")) {
                title = new File(song).getName();
                if (title == null || title.length() < 1 || title.equals("null"))
                    title = "UnKnown";
            }

            String artist = mmr
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            if (artist == null || artist.length() < 1 || artist.equals("null"))
                artist = "Unknown";

            final StringBuilder builder = new StringBuilder();
            builder.append(title);
            builder.append(SEP);
            builder.append(artist);

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Unknown" + SEP + "Unknown";
    }

    public static String getRawFileSongName(String song) {
        return "Colossus" + SEP + "Meizong";
    }

    public static String formatNumber(int i) {
        return NumberFormat.getIntegerInstance().format(i);
    }

    public static SecurePreferences getPrefs(Context context) {
        return new SecurePreferences(context, "prefs_tbs_n", "X5TBSSDVSHYGF",
                true);
    }
}
