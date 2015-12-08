package tbs.bassjump.reference;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Date;

import tbs.bassjump.Utility;

public class Game extends ApplicationAdapter {
    // SAVE STATE:
    private static final Color color = new Color();
    private static final GlyphLayout glyphLayout = new GlyphLayout();
    private static final GameController controller = new GameController();
    private static final Date date = new Date();
    public static Level level;
    public static GameState state;
    public static int score;
    public static Player player;
    // TEXT ANIMATOR:
    public static TextAnimator textAnimator;
    public static float scoreTextMult;
    public static float coinTextMult;
    // TMPS:
    public static String textToMeasure;
    // MENU BUTTONS:
    public static CanvasButton rateButton;
    public static CanvasButton leaderButton;
    public static CanvasButton achievementButton;
    public static CanvasButton storeButton;
    public static CanvasButton homeButton;
    public static CanvasButton shareButton;
    public static CanvasButton homeButton2;
    public static CanvasButton retryButton;
    public static CanvasButton adButton;
    public static CanvasButton likeButton;
    public static CanvasButton buyButton;
    public static CanvasButton reviveButton;
    public static CanvasButton gambleButton;
    public static int w, h;
    // STORE:
    public static ArrayList<StoreItem> storeItems;
    // SOUNDS:
    public static Sound jumpSound, coinSound, buttonSound, moneySound, deathSound, winSound;
    public static Music ambientMusic;
    // ADS:
//    public static InterstitialAd interstitial;
    public static boolean loadedBigAd;
    public static int showAdInt;
    // INTRO SCREEN:
    public static int introAlpha;
    public static float introCountdown;
    // REVIVAL:
    public static int revivalCost;
    // CASINO:
    public static CasinoManager casinoManager;
    private static Texture buttonAtlas;
    private static SpriteBatch batch;
    private static ShapeRenderer renderer;
    private static BitmapFont font;

    public static void setup() {
        // SETUP:
        state = GameState.Menu;
        level.setup();
        player.setup();

        score = 0;
        casinoManager = new CasinoManager();

        // ANIMATOR:
        scoreTextMult = 1;
        coinTextMult = 1;

        player.loadData();
        casinoManager.updateCost(player.coins);
    }

    public static void showAd(final boolean manual) {


    }

    private void getSprites() {
        dispose();
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        font = Utility.getFont();
        getCanvasButtons();
        Gdx.input.setInputProcessor(controller);
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.mp3"));
        buttonSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));
        moneySound = Gdx.audio.newSound(Gdx.files.internal("money.mp3"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("death2.mp3"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
//        ambientMusic = Gdx.audio.newMusic(Gdx.files.internal("ambient.mp3"));
//        ambientMusic.setLooping(true);
//        ambientMusic.play();
    }

    public static void draw() {
        // GAME:
        level.draw(renderer);
        player.draw(renderer);
        if (state != GameState.Playing) {
            color.set(0x000000aa);
            renderer.setColor(color);
            renderer.rect(0, 0, w, h);
        }
    }

    private static void drawHUD() {
        textAnimator.draw(batch);
        float scale;

        // SCORE:
        if (state == GameState.Playing) {
            scale = Utility.getScale(GameValues.SCORE_TEXT_SIZE * scoreTextMult);

            font.getData().setScale(scale);
            textToMeasure = "" + player.score;
            glyphLayout.setText(font, textToMeasure);
            color.set(0xFFFFFFFF);

            float scoreBottom = h - glyphLayout.height - (GameValues.TEXT_PADDING / 3);
            float scoreW = glyphLayout.width;
            float scoreX = w - GameValues.TEXT_PADDING - (scoreW / 2);
            Utility.drawCenteredText(batch, color, textToMeasure, scoreX, scoreBottom + (glyphLayout.height / 2), scale);

            scale = Utility.getScale(GameValues.SCORE_TEXT_SIZE / 2.5f);
            font.getData().setScale(scale);
            textToMeasure = "SCORE";
            glyphLayout.reset();
            glyphLayout.setText(font, textToMeasure);
            color.set(GameValues.RING_COLOR);
            Utility.drawCenteredText(batch, color, "SCORE", scoreX - (scoreW / 2) - (glyphLayout.width / 2) - (GameValues.TEXT_PADDING / 2), scoreBottom + glyphLayout.height / 2, scale);
        }

        // DRAW MENU:
        if (state == GameState.Menu) {
            scale = Utility.getScale(GameValues.MENU_TEXT_SIZE);
            font.getData().setScale(scale);

            glyphLayout.setText(font, "T");
            float textHeight = glyphLayout.height;
            float textBottom = h - textHeight;

            // MENU TEXT:
            color.set(0xe6e8f1FF);
            Utility.drawCenteredText(batch, color, "TOO HARD?", w / 2, textBottom + (textHeight / 2), scale);

            glyphLayout.setText(font, "T");
            textHeight = glyphLayout.height;

            color.set(1, 1, 1, 120 / 255f);
            Utility.drawCenteredText(batch, color, "CAN YOU GET TO 100?", w / 2, textBottom - (GameValues.TEXT_PADDING / 8) - (textHeight / 4), scale / 2);
            Utility.drawCenteredText(batch, color, "TAP TO BEGIN", w / 2, leaderButton.y + leaderButton.height + GameValues.TEXT_PADDING + (textHeight / 4), scale / 2);

            // BUTTONS:
            rateButton.draw(batch);
            leaderButton.draw(batch);
            achievementButton.draw(batch);
            storeButton.draw(batch);
            gambleButton.draw(batch);
            if (gambleButton.active) {
                color.set(1, 1, 1, 120 / 255f);
                Utility.drawCenteredText(batch, color, casinoManager.playCost + " COINS", w / 2, gambleButton.y - (GameValues.TEXT_PADDING * 0.85f), Utility.getScale(GameValues.MENU_TEXT_SIZE_2 / 2));
            }
        }

        // DRAW DEATH:
        if (state == GameState.Death) {
            // DEATH TEXT:
            scale = Utility.getScale(GameValues.MENU_TEXT_SIZE);
            glyphLayout.setText(font, "YOU DIED");
            float bottom = h - glyphLayout.height - GameValues.TEXT_PADDING;
            color.set(0xe6e8f1FF);
            Utility.drawCenteredText(batch, color, "YOU DIED", w / 2, bottom + (glyphLayout.height / 2), scale);
            color.set(1, 1, 1, 120 / 250f);
            Utility.drawCenteredText(batch, color, "GET TO 100", w / 2, bottom - (1.3f * GameValues.TEXT_PADDING) - (glyphLayout.height / 2), scale / 2);
            // AFTER GAME INFO:

            scale = Utility.getScale(GameValues.MENU_TEXT_SIZE_2);
            bottom = homeButton.y + homeButton.height + GameValues.TEXT_PADDING;
            glyphLayout.setText(font, "T");
            Utility.drawCenteredText(batch, color, "SCORE: " + player.score, w / 2, bottom + (glyphLayout.height / 2), scale);

            color.set(0xffffffff);
            Utility.drawCenteredText(batch, color, "BEST: " + player.highScore, w / 2, bottom + (1.35f * glyphLayout.height), scale);

            // BUTTONS:
            homeButton.draw(batch);
            shareButton.draw(batch);
            retryButton.draw(batch);
            adButton.draw(batch);
            buyButton.draw(batch);
            reviveButton.draw(batch);

            if (!buyButton.active)
                likeButton.draw(batch);

            if (reviveButton.active) {
                color.set(1, 1, 1, 120 / 250f);
                Utility.drawCenteredText(batch, color, revivalCost + " COINS", w / 2, reviveButton.y - (GameValues.TEXT_PADDING * 0.85f), Utility.getScale(GameValues.MENU_TEXT_SIZE / 2.5f));
            }
        }

        // DRAW STORE:

        if (state == GameState.Store) {
            // DEATH TEXT:
            color.set(0xe6e8f1FF);
            Utility.drawCenteredText(batch, color, "STORE", w / 2, h - (h / 8), Utility.getScale(GameValues.MENU_TEXT_SIZE));

            color.set(1, 1, 1, 120 / 250f);
            Utility.drawCenteredText(batch, color, "BUY NEW ITEMS", w / 2, h - ((h / 8) + (GameValues.TEXT_PADDING * 1.5f)), Utility.getScale(GameValues.MENU_TEXT_SIZE / 2));
        }

        // DRAW CASINO

        if (state == GameState.Casino) {
            // CASINO HEADER:
            color.set(0xFFe6e8f1);
            Utility.drawCenteredText(batch, color, "REWARDS", w / 2, h - (h / 8), Utility.getScale(GameValues.MENU_TEXT_SIZE));
            color.set(1, 1, 1, 120 / 250f);
            Utility.drawCenteredText(batch, color, "SELECT ONE", w / 2, h - ((h / 8) + (GameValues.TEXT_PADDING * 1.5f)), Utility.getScale(GameValues.MENU_TEXT_SIZE / 2));

            // DRAW CASINO:
            casinoManager.draw(batch);
            homeButton2.draw(batch);
        }

        // COINS:
        color.set(0xFFFFFFFF);
        textToMeasure = player.coins + "";
        scale = Utility.getScale(GameValues.COIN_TEXT_SIZE * coinTextMult);
        font.getData().setScale(scale);

        glyphLayout.setText(font, textToMeasure);
        final float textWidth = glyphLayout.width;

        Utility.drawCenteredText(batch, color, textToMeasure, GameValues.TEXT_PADDING + (glyphLayout.width / 2), GameValues.TEXT_PADDING + (glyphLayout.height / 2), scale);

        if (state == GameState.Playing)
            color.set(GameValues.RING_COLOR);
        else {
            color.set(1, 1, 1, 120 / 255f);
        }

        scale = Utility.getScale(GameValues.COIN_TEXT_SIZE / 2.5f);
        font.getData().setScale(scale);
        glyphLayout.setText(font, "COINS");
        Utility.drawCenteredText(batch, color, "COINS", (1.75f * GameValues.TEXT_PADDING) + textWidth + (glyphLayout.width / 2), GameValues.TEXT_PADDING + (glyphLayout.height / 2), scale);

        // DRAW INTRO:
        if (introAlpha > 0) {
            // DRAW INTRO:
            batch.end();
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            color.set(GameValues.BACKGROUND_COLOR);
            color.a = introAlpha / 255f;
            renderer.setColor(color);
            renderer.rect(0, 0, w, h);
            renderer.end();
            batch.begin();
            color.set(0xffffffff);
            Utility.drawCenteredText(batch, color, "THE BIG SHOTS", w / 2, h / 2, Utility.getScale(GameValues.MENU_TEXT_SIZE / 1.5f));
        }
    }


    public static void update(float delta) {
        level.update(delta);
        player.update(delta);

        // TEXT ANIMATION:
        textAnimator.update(delta);
        if (scoreTextMult > 1) {
            scoreTextMult -= delta * 0.0025;
            if (scoreTextMult <= 1)
                scoreTextMult = 1;
        }
        if (coinTextMult > 1) {
            coinTextMult -= delta * 0.0025;
            if (coinTextMult <= 1)
                coinTextMult = 1;
        }

        // UPDATE CASINO:
        if (state == GameState.Casino) {
            casinoManager.update(delta);
        }

        // UPDATE BUTTONS YOU WANT TO ANIMATE:
        adButton.update(delta);
        likeButton.update(delta);
        buyButton.update(delta);
        rateButton.update(delta);
        reviveButton.update(delta);
        gambleButton.update(delta);

        if (introCountdown > 0) {
            introCountdown -= 1;
            if (introCountdown < 0)
                introCountdown = 0;
        } else if (introAlpha > 0) {
            introAlpha -= 10;
            if (introAlpha < 0)
                introAlpha = 0;
        }
    }

    private static void init() {
        // ONCE:
        setupStore();
        level = new Level();
        player = new Player();
        textAnimator = new TextAnimator();

        reviveButton.up = false;
        buyButton.playSound = false;
        gambleButton.playSound = false;
        gambleButton.up = false;

        // AD:
        showAdInt = 0;
        loadedBigAd = false;

        // INTRO:
        introCountdown = 100;
        introAlpha = 255;

        // SETUP ETC:
        setup();
    }

    private static void getCanvasButtons() {
        buttonAtlas = new Texture("buttons.png");
        // SETUP BUTTONS:
        rateButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 256, 256, 256, 256), (w / 2) - (GameValues.MENU_BTN_WIDTH / 2), h - ((h / 2) + GameValues.BUTTON_PADDING * 4), false);
        leaderButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 256, 512, 256, 256), (int) rateButton.x - (GameValues.MENU_BTN_WIDTH + GameValues.BUTTON_PADDING), h - ((h / 2) + GameValues.BUTTON_PADDING * 4), false);
        achievementButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 256, 0, 256, 256), (int) rateButton.x + (GameValues.MENU_BTN_WIDTH + GameValues.BUTTON_PADDING), h - ((h / 2) + GameValues.BUTTON_PADDING * 4), false);
        storeButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 512, 0, 256, 256), (int) (w - (GameValues.MENU_BTN_WIDTH + GameValues.BUTTON_PADDING / 1.5f)), (int) GameValues.TEXT_PADDING, false);
        shareButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 768, 0, 256, 256), (int) rateButton.x - (GameValues.MENU_BTN_WIDTH + GameValues.BUTTON_PADDING), h - ((h / 2) + (GameValues.BUTTON_PADDING * 4)), false);
        homeButton2 = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 512, 512, 256, 256), (int) (w - (GameValues.MENU_BTN_WIDTH + GameValues.BUTTON_PADDING / 1.5f)), (int) GameValues.TEXT_PADDING, false);
        homeButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 512, 512, 256, 256), (w / 2) - (GameValues.MENU_BTN_WIDTH / 2), h - ((h / 2) + (GameValues.BUTTON_PADDING * 4)), false);
        retryButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 0, 256, 256, 256), (int) rateButton.x + (GameValues.MENU_BTN_WIDTH + GameValues.BUTTON_PADDING), h - ((h / 2) + (GameValues.BUTTON_PADDING * 4)), false);
        adButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 0, 0, 256, 256), (int) (w - (GameValues.MENU_BTN_WIDTH + GameValues.BUTTON_PADDING / 1.5f)), (int) GameValues.TEXT_PADDING, true);
        likeButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 512, 256, 256, 256), (int) (w - (GameValues.MENU_BTN_WIDTH + GameValues.BUTTON_PADDING / 1.5f)), (int) GameValues.TEXT_PADDING, true);
        buyButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 0, 512, 256, 256), (int) (w - (GameValues.MENU_BTN_WIDTH + GameValues.BUTTON_PADDING / 1.5f)), (int) GameValues.TEXT_PADDING, true);
        reviveButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 0, 768, 256, 256), (w / 2) - (GameValues.MENU_BTN_WIDTH / 2), h - ((h / 2) - (int) (GameValues.MENU_BTN_WIDTH * 1.75f)), true);
        gambleButton = new CanvasButton(new TextureAtlas.AtlasRegion(buttonAtlas, 768, 512, 256, 256), (w / 2) - (GameValues.MENU_BTN_WIDTH / 2), h - ((h / 2) - (int) (GameValues.MENU_BTN_WIDTH * 1.75f)), true);
    }

    private static void setupStore() {
        // STORE:
        casinoManager = new CasinoManager();
        storeItems = new ArrayList<StoreItem>();
        // SKINS:
        storeItems.add(new StoreItem(10, 0xff8c8cff, 0xffababff, false, 0, false)); // 0
        storeItems.add(new StoreItem(10, 0x948cffff, 0xc0abffff, false, 0, false)); // 1
        storeItems.add(new StoreItem(10, 0xc2ff8cff, 0xd2ffabff, false, 0, false)); // 2
        storeItems.add(new StoreItem(10, 0xffff8cff, 0xf9ffabff, false, 0, false)); // 3
        storeItems.add(new StoreItem(25, 0xf08cffff, 0xf4abffff, false, 0, false)); // 4
        storeItems.add(new StoreItem(25, 0xff8ce8ff, 0xffabeeff, false, 0, false)); // 5
        storeItems.add(new StoreItem(50, 0xffab4aff, 0xffc17aff, false, 0, false)); // 6
        storeItems.add(new StoreItem(50, 0xff477aff, 0xff99b5ff, false, 0, false)); // 7
        storeItems.add(new StoreItem(50, 0x6699b5ff, 0xc2d7e3ff, false, 0, false)); // 8
        storeItems.add(new StoreItem(50, 0x69d7e3ff, 0xc7e0e3ff, false, 0, false)); // 9
        storeItems.add(new StoreItem(75, 0xe4ef37ff, 0xeaef9aff, false, 0, false)); // 10
        storeItems.add(new StoreItem(75, 0x23ef4bff, 0xe6ffebff, false, 0, false)); // 11
        storeItems.add(new StoreItem(100, 0x999999ff, 0xb3b3b3ff, false, 0, false)); // 12
        storeItems.add(new StoreItem(100, 0xcbcbcbff, 0xffffffff, false, 0, false)); // 13
        storeItems.add(new StoreItem(150, 0xeb767fff, 0xeb767fff, false, 0, false)); // 14
        storeItems.add(new StoreItem(150, 0x81eb66ff, 0x81eb66ff, false, 0, false)); // 15
        storeItems.add(new StoreItem(200, 0x66b6ebff, 0x66b6ebff, false, 0, false)); // 16
        storeItems.add(new StoreItem(200, 0x575757ff, 0xf9f9f9ff, false, 0, false)); // 17
        storeItems.add(new StoreItem(500, 0x575757ff, 0xeb6973ff, false, 0, false)); // 18
        storeItems.add(new StoreItem(500, 0x575757ff, 0x81eb66ff, false, 0, false)); // 19
        storeItems.add(new StoreItem(1000, 0xFFFFFFff, 0xffababff, false, 0, false)); // 20
        storeItems.add(new StoreItem(1000, 0x363636ff, 0x6a6a6aff, false, 0, false)); // 21
        storeItems.add(new StoreItem(1000, 0xFFFFFFff, 0x4cde5aff, false, 0, false)); // 22
        storeItems.add(new StoreItem(1000, 0x4c5fdeff, 0x55de4cff, false, 0, false)); // 23
        storeItems.add(new StoreItem(1000, 0xde4c4cff, 0xe3ed71ff, false, 0, false)); // 24
        storeItems.add(new StoreItem(1000, 0xe3ed5bff, 0xe37a5bff, false, 0, false)); // 25
        storeItems.add(new StoreItem(10000, 0xFFFFFFff, 0x000000ff, false, 0, false)); // 26
        storeItems.add(new StoreItem(10000, 0x000000ff, 0xFFFFFFff, false, 0, false)); // 27

        storeItems.add(new StoreItem(-1, 0xed2df9ff, 0x415ef9ff, false, 5, false)); // 28
        storeItems.add(new StoreItem(-1, 0x516aecff, 0xFFFFFFff, false, 10, false));  // 29
        storeItems.add(new StoreItem(-1, 0xec5151ff, 0xFFFFFFff, false, 100, false));  // 30
        storeItems.add(new StoreItem(-1, 0x9f6236ff, 0x9f62beff, false, 500, false));  // 31
        storeItems.add(new StoreItem(-1, 0xffbb00ff, 0xff2800ff, false, 1000, false)); // 32
        storeItems.add(new StoreItem(-1, 0xffff93ff, 0xffffe8ff, false, 10000, false)); // 33
        // TRAILS:
        storeItems.add(new StoreItem(10, 0xFFFFFFff, -1, false, 0, true)); // 34
        storeItems.add(new StoreItem(10, 0x363636ff, -1, false, 0, true)); // 35
        storeItems.add(new StoreItem(100, 0xc2ff8cff, -1, false, 0, true)); // 36
        storeItems.add(new StoreItem(100, 0x4c5fdeff, -1, false, 0, true)); // 37
        storeItems.add(new StoreItem(1000, 0xde4c4cff, -1, false, 0, true)); // 38
        storeItems.add(new StoreItem(1000, 0xe3ed5bff, -1, false, 0, true)); // 39
        storeItems.add(new StoreItem(10000, 0x2ded28ff, -1, false, 0, true)); // 40
        storeItems.add(new StoreItem(10000, 0xffab4aff, -1, false, 0, true)); // 41
        storeItems.add(new StoreItem(-1, 0xffbb00ff, -1, false, 2, true)); // 42
        storeItems.add(new StoreItem(-1, 0xffff93ff, -1, false, 100, true)); // 43
        storeItems.add(new StoreItem(-1, 0xed9c28ff, -1, false, 1000, true)); // 44
        storeItems.add(new StoreItem(-1, 0xed28d8ff, -1, false, 10000, true)); // 45

        final StoreItem removeAds = new StoreItem(0, 0x000000ff, 0x000000ff, false, 0, false);
        removeAds.text = "Remove Ads";
        removeAds.IAPID = GameValues.removeAdsID; // PUT ID HERE
        storeItems.add(removeAds);
       final StoreItem buyCoins = new StoreItem(0, 0x000000ff, 0x000000ff, false, 0, false);
        buyCoins.text = "Buy Coins";
        buyCoins.IAPID = GameValues.buyCoinsID; // PUT ID HERE
        storeItems.add(buyCoins);
    }


    // STORE DIALOG:
    public static void showStore() {

    }

    public static void log(String msg) {
        date.setTime(System.currentTimeMillis());
        Gdx.app.error("" + date.toString(), msg);
    }

    @Override
    public void dispose() {
        try {
            batch.dispose();
        } catch (Exception e) {

        }
        try {
            renderer.dispose();
        } catch (Exception e) {

        }
        try {
            buttonAtlas.dispose();
        } catch (Exception e) {

        }
        try {
            jumpSound.dispose();
        } catch (Exception e) {

        }
        try {
            coinSound.dispose();
        } catch (Exception e) {
        }
        try {
            buttonSound.dispose();
        } catch (Exception e) {

        }
        try {
            moneySound.dispose();
        } catch (Exception e) {

        }
        try {
            deathSound.dispose();
        } catch (Exception e) {

        }
        try {
            winSound.dispose();
        } catch (Exception e) {

        }
        try {
            ambientMusic.dispose();
        } catch (Exception e) {

        }
        Utility.disposeFont();
        super.dispose();
    }

    @Override
    public void resume() {
        super.resume();
        getSprites();
    }

    @Override
    public void create() {
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        getSprites();
        init();
    }

    @Override
    public void render() {
        color.set(GameValues.BACKGROUND_COLOR);
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        update(Gdx.graphics.getDeltaTime() * 1000);

        try {
            renderer.begin(ShapeRenderer.ShapeType.Filled);
        } catch (Exception e) {
            e.printStackTrace();
        }
        draw();
        try {
            renderer.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            batch.begin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        drawHUD();
        try {
            batch.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
