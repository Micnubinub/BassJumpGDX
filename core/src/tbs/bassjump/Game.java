package tbs.bassjump;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Random;

import tbs.bassjump.animation.MovingText;
import tbs.bassjump.fragments.GetCoinsFragment;
import tbs.bassjump.levels.Level;
import tbs.bassjump.managers.BitmapLoader;
import tbs.bassjump.objects.AnimCircle;
import tbs.bassjump.objects.Player;
import tbs.bassjump.utility.AdManager;
import tbs.bassjump.utility.GameObject;
import tbs.bassjump.utility.GameUtils;


public class Game extends ApplicationAdapter {
    // PAINTER:
    private static final Color c = new Color();
    //MusicShuffle
    private static final Random random = new Random();
    private static final int background = 0xff222222;
    public static int[] colors = new int[]{0xffbb00};
    // CONTEXT
    // LEVEL AND PLAYER:
    public static Player player; // PLAYER CONTAINS PLAYER INFO
    public static Level level; // LEVEL CONTAINS LEVEL OBJECTS
    // MUSIC
    public static int alphaM, delta;
    public static boolean isMusicEnabled;
    // STATE
    public static GameState state;
    public static GameObject leaderBtn;
    public static GameObject rateBtn;
    public static GameObject modeBtn;
    public static GameObject storeBtn;
    public static GameObject soundBtn;
    public static GameObject achievBtn;
    public static GameObject shareBtn;
    public static String[] songs;
    // COLORS:
    public static int color; // CHANGE TO INT
    // STORE
    public static boolean introShowing;
    // MODE
    public static GameMode mode;
    // renderer DATA;
    // SPECIAL CONSTANTS:
    public static String txt;
    public static String scoreText;
    public static boolean drawTop;
    public static boolean drawBottom;
    public static boolean prepared;
    // SOUND && VISUALIZER:
    public static Music ambientMusic;
    public static int numberOfPlayNextSongRetries;
    public static float scoreTextMult;
    public static AdManager adManager;
    public static boolean showAds;
    public static GetCoinsFragment getCoinsFragment = new GetCoinsFragment();
    //    private static final ArrayList<ValueAnimator> animations = new ArrayList<ValueAnimator>(10);
    public static int w, h;
    protected static OrthographicCamera camera;
    protected static SpriteBatch batch;
    protected static ShapeRenderer renderer;
    private static String currSong;
    // MOVING TEXTS:
    private static ArrayList<MovingText> animatedTexts; // ANIMATED TEXT LIST
    private static int animatedTextIndex; // WHICH TEXT TO USE
    // INTERFACE:
    private static GameObject scoreDisplay;
    // GLOBAL PARTICLES:
    private static ArrayList<AnimCircle> circles;
    private static int circleIndex;

    /* Todo 'security find-identity -v -p codesigning' for iosSignIdentity

      robovm {
     Configure robovm
    iosSignIdentity = "LZU8BZAM9B.the.bigshots.lostplanet"
    iosProvisioningProfile = "path/to/profile"
    iosSkipSigning = false
    stdoutFifo = ""
    stderrFifo = ""
}
     packaging for any platform navigate to the root
     packaging desktop version>> gradlew desktop:dist
     packaging android version>> gradlew android:assembleRelease
     linux/ios chmod 755 gradlew
     packaging ios version>> gradlew ios:createIPA
     packaging web version>> gradlew html:dist
     TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(colorTexture, x, y, width, height)
     bring for the following classes > Screen, Utility*/
    // ANIMATION
//    private static String songName;
    // INTRO
    private static int introDelay;
    private static int loadProg;
    private static int loadWidth;
    // RANKING:
//    private static LeaderboardScore leaderboard;
    private static BitmapLoader bitmapLoader;

    public static void setup() {
        long tic = System.currentTimeMillis();
//        Utility.log("tbs.bassjump.reference.Game Initialized");
        // MUSIC
        alphaM = 0;
        // MENU
        // menuTextAlpha = 255;

        // SPEED CALC
        GameValues.SPEED_FACTOR_ORIGINAL = ((float) h / 600);

//        Utility.log("SPEED: " + GameValues.SPEED_FACTOR_ORIGINAL);

        player = new Player();

        String shape = Utility.getString(
                GameUtils.EQUIPPED_SHAPE);
        if (shape == null || shape.length() < 2)
            shape = GameUtils.SHAPE_RECTANGLE;
        Player.setPlayerShape(GameUtils.getShapeType(shape));

        level = new Level();
        setupGame();

        setupInterface();
//        Log.e("setUp ticToc = ", String.valueOf(System.currentTimeMillis() - tic));
    }


    protected static void clear() {
        Gdx.gl.glClearColor(.22f, .22f, .22f, 1);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
    }

    public static void setupGame() {
        // SETUP NEW GAME
        // ADS

        if (Game.showAds) {
            //Todo load ads here
            if ((player.gamesPlayed + 1) % 10 == 0 && player.gamesPlayed > 0) {
                // AD WARNING:

//                        Utility.showToast("Ad Showing!", con);

            }

            if (player.gamesPlayed % 10 == 0 && player.gamesPlayed > 0) {
                //Todo show ad here

            }
        }
        GameValues.SPEED_BONUS = 1;

        // COLORS:
        color = colors[random.nextInt(colors.length)];

        // SAVE SCORE TO LB
//        if (MainActivity.getApiClient().isConnected()) {
//            if (player.highScoreA > 0)
//                Games.Leaderboards.submitScore(MainActivity.getApiClient(),
//                        "CgkIvYbi1pMMEAIQBg", player.highScoreA);
//            if (player.highScoreR > 0)
//                Games.Leaderboards.submitScore(MainActivity.getApiClient(),
//                        "CgkIvYbi1pMMEAIQBw", player.highScoreR);
//            if (player.highScoreU > 0)
//                Games.Leaderboards.submitScore(MainActivity.getApiClient(),
//                        "CgkIvYbi1pMMEAIQEQ", player.highScoreU);
//            if (player.highScoreS > 0)
//                Games.Leaderboards.submitScore(MainActivity.getApiClient(),
//                        "CgkIvYbi1pMMEAIQEg", player.highScoreS);
//            if (player.highScoreS2 > 0)
//                Games.Leaderboards.submitScore(MainActivity.getApiClient(),
//                        "CgkIvYbi1pMMEAIQFA", player.highScoreS);
//
//        }

        // PLAYER & LEVEL
        state = GameState.Menu;
        level.setup(); // Setup Before Player: Position Reasons
        player.setup();

        // TEXT
        animatedTexts = new ArrayList<MovingText>();
        for (int i = 0; i < 5; ++i) {
            animatedTexts.add(new MovingText());
        }
        scoreTextMult = 1;

        // CHECK ACHIEVEMENTS:
        checkAchievements();
    }

    private static void rectangle(ShapeRenderer renderer, int x, int y, int w,
                                  int h, boolean drawLeft, boolean drawRight, boolean drawTop,
                                  boolean drawBottom) {
        if (drawLeft)
            renderer.rect(x, y, x, y + h);

        if (drawRight)
            renderer.rect(x + w, y, x + w, y + h);

        if (drawTop)
            renderer.rect(x, y, x + w, y);

        if (drawBottom)
            renderer.rect(x, y + h, x + w, y + h);

    }

    private static void checkAchievements() {
        // SCORE RELATED:
//        if (MainActivity.getApiClient().isConnected()) {
//            if (player.highScoreA >= 10)
//                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQAQ");
//            if (player.highScoreA >= 50)
//                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQCA");
//            if (player.highScoreA >= 100)
//                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQCg");
//            if (player.highScoreA >= 200)
//                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQDQ");
//            if (player.highScoreR >= 10)
//                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQAg");
//            if (player.highScoreR >= 50)
//                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQCQ");
//
//            // DEATH & GAMES RELATED:
//            if (player.gamesPlayed >= 1000)
//                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQAw");
//            if (player.gamesPlayed >= 500)
//                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQBA");
//
//            // OTHER
//            MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQDA");
//        }
    }

    public static void showAnimatedText(String text, int x, int y, int spd,
                                        int am, int a, int grwth) {
        try {
            animatedTexts.get(animatedTextIndex).activate(text, x, y, spd, am,
                    a, grwth);
            animatedTextIndex += 1;
            if (animatedTextIndex == 3)
                animatedTextIndex = 0;
        } catch (IndexOutOfBoundsException e) {
            animatedTexts.get(0).activate(text, x, y, spd, am, a, grwth);
        }
    }

    private static void setupInterface() {
        // BUTTONS && IMAGES:
        scoreDisplay = new GameObject();
        scoreDisplay.xPos = w / 2;
        scoreDisplay.scale = h / 13;
        scoreDisplay.scale2 = (int) (scoreDisplay.scale * 1.25f);
        scoreDisplay.yPos = (int) (scoreDisplay.scale * 1.35f);

        // BUTTONS:
        leaderBtn = new GameObject();
        leaderBtn.scale = GameValues.BUTTON_SCALE;
        leaderBtn.xPos = (w - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        leaderBtn.yPos = GameValues.BUTTON_PADDING;

        rateBtn = new GameObject();
        rateBtn.scale = GameValues.BUTTON_SCALE;
        rateBtn.xPos = (w - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        rateBtn.yPos = GameValues.BUTTON_SCALE
                + (GameValues.BUTTON_PADDING * 2);

        modeBtn = new GameObject();
        modeBtn.scale = GameValues.BUTTON_SCALE;
        modeBtn.xPos = (w - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        modeBtn.yPos = (GameValues.BUTTON_SCALE * 2)
                + (GameValues.BUTTON_PADDING * 3);

        achievBtn = new GameObject();
        achievBtn.scale = GameValues.BUTTON_SCALE;
        achievBtn.xPos = GameValues.BUTTON_PADDING;
        achievBtn.yPos = modeBtn.yPos;

        soundBtn = new GameObject();
        soundBtn.scale = GameValues.BUTTON_SCALE;
        soundBtn.xPos = (w - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        soundBtn.yPos = (h - GameValues.BUTTON_SCALE)
                - (GameValues.BUTTON_PADDING);

        storeBtn = new GameObject();
        storeBtn.scale = GameValues.BUTTON_SCALE;
        storeBtn.xPos = (w - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        storeBtn.yPos = (soundBtn.yPos - GameValues.BUTTON_SCALE)
                - (GameValues.BUTTON_PADDING);

        shareBtn = new GameObject();
        shareBtn.scale = GameValues.BUTTON_SCALE;
        shareBtn.xPos = GameValues.BUTTON_PADDING;
        shareBtn.yPos = soundBtn.yPos;

        // MOVING TEXT:
        animatedTextIndex = 0;
        animatedTexts = new ArrayList<MovingText>();
        for (int i = 0; i < 10; ++i) {
            animatedTexts.add(new MovingText());
        }
    }

    public static void showCircle(int scale, int x, int y, int a, boolean special) {
        circleIndex += 1;
        if (circleIndex == circles.size())
            circleIndex = 0;
        circles.get(circleIndex).activate(scale, x, y, a, special);
    }

    // FAKE LOADER:
    private static void loadProg(int loadProg) {
        float pcrn = loadProg * 100 / 150;
        if (pcrn < 0)
            pcrn = 0;
        loadWidth = (int) ((GameValues.LOADING_BAR_WIDTH * (pcrn / 100)));
    }

    public static void log(String log) {
        Gdx.app.log("game", log);
    }

    public static void pauseMusic() {
        ambientMusic.pause();
    }

    public static void playMusic() {
        ambientMusic.play();
    }


    public void update() {
        delta = (int) (Gdx.graphics.getDeltaTime() * 1000);
        GameValues.SPEED_FACTOR = (int) ((GameValues.SPEED_FACTOR_ORIGINAL * GameValues.SPEED_BONUS) * delta);
        if (GameValues.SPEED_FACTOR < 1)
            GameValues.SPEED_FACTOR = 1;
        GameValues.PLAYER_JUMP_SPEED = (GameValues.SPEED_FACTOR * GameValues.PLAYER_JUMP_SPEED_MULT);
        if (introShowing) {
            introDelay -= 1;
            loadProg += 1;
            loadProg(loadProg);
            if (introDelay <= 0) {
                introShowing = false;
            }
        } else {
            introShowing = false;
        }

        player.update();
        level.update();
        for (int i = 0; i < animatedTexts.size(); ++i) {
            animatedTexts.get(i).update();
        }
        for (int i = 0; i < circles.size(); ++i) {
            circles.get(i).update();
        }
        // M:
        if (alphaM > 0) {
            alphaM -= 15;
            if (alphaM < 0)
                alphaM = 0;
        }

        // ANIM:
        scoreTextMult -= 0.05f;
        if (scoreTextMult < 1) {
            scoreTextMult = 1;
        }
    }

    public void init() {
// CONST
        log("initCalled");
        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(w, h);

        // ACHIEVEMENT:
//        unlockAchievement("CgkIvYbi1pMMEAIQDA");

        // SETUP:
        Game.setup();
        // LOAD AD:
        Game.adManager.loadFullscreenAd();

        // LOAD DATA:
        showAds = Utility.getString("nerUds") == null;

        if (Utility.getString("hScore") != null) {
            Game.player.highScoreA = Integer.parseInt(Utility
                    .getString("hScore"));
        } else {
            Game.player.highScoreA = 0;
        }
        if (Utility.getString("hScoreR") != null) {
            Game.player.highScoreR = Integer.parseInt(Utility
                    .getString("hScoreR"));
        } else {
            Game.player.highScoreR = 0;
        }
        if (Utility.getString("hScoreU") != null) {
            Game.player.highScoreU = Integer.parseInt(Utility
                    .getString("hScoreU"));
        } else {
            Game.player.highScoreR = 0;
        }
        if (Utility.getString("hScoreS") != null) {
            Game.player.highScoreS = Integer.parseInt(Utility
                    .getString("hScoreS"));
        } else {
            Game.player.highScoreS = 0;
        }
        if (Utility.getString("hScoreS2") != null) {
            Game.player.highScoreS2 = Integer.parseInt(Utility
                    .getString("hScoreS2"));
        } else {
            Game.player.highScoreS2 = 0;
        }

        if (Utility.getString("musicOn") != null) {
            if (Utility.getString("musicOn").equals("off")) {
                Game.isMusicEnabled = false;
                Game.pauseMusic();
            } else {
                Game.isMusicEnabled = true;
            }
        } else {
            Game.isMusicEnabled = true;
        }

        if (Utility.getString("gMode") != null) {
            if (Utility.getString("gMode").equals("arcade")) {
                Game.mode = GameMode.Arcade;
            } else if (Utility.getString("gMode").equals("recruit")) {
                Game.mode = GameMode.Recruit;
            } else if (Utility.getString("gMode").equals("ultra")) {
                Game.mode = GameMode.Ultra;
            } else if (Utility.getString("gMode").equals("singul")) {
                Game.mode = GameMode.Singularity;
            } else if (Utility.getString("gMode").equals("speed")) {
                Game.mode = GameMode.SpeedRunner;
                GameValues.PLAYER_JUMP_SPEED_MULT = 8;
            }
        } else {
            Game.mode = GameMode.Arcade;
        }
        if (Utility.getString("gPlayed") != null) {
            Game.player.gamesPlayed = Integer.parseInt(Utility
                    .getString("gPlayed")) + 1;
        } else {
            Game.player.gamesPlayed = 0;
        }


        long tic = System.currentTimeMillis();
        ambientMusic = Gdx.audio.newMusic(Gdx.files.internal("ambient.mp3"));
        ambientMusic.setLooping(true);
        ambientMusic.play();

        circles = new ArrayList<AnimCircle>();
        circleIndex = 0;
        for (int i = 0; i < 2; ++i) {
            circles.add(new AnimCircle());
        }

        // LOAD IMAGES ONCE
        bitmapLoader = new BitmapLoader();

        introDelay = 150;
        loadProg = 0;
        loadWidth = 0;
        introShowing = true;
//  Todo      adManager = new AdManager(con);
//   Todo     Utility.addGameColors();

    }

    @Override
    public void render() {
        c.set(background);
        Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        update();

        try {
            renderer.begin(ShapeRenderer.ShapeType.Filled);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onDraw();
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

    public void onDraw() {
        // DRAW EVERYTHING IN ORDER:
        // c.set(0x000000); // DEFAULT

        c.set(0xffff12ff);
        for (int i = 0; i < level.speedParticles.size(); ++i) {
            renderer.rect(level.speedParticles.get(i).xPos,
                    level.speedParticles.get(i).yPos,
                    level.speedParticles.get(i).xPos
                            + GameValues.SPEED_PARTICLE_WIDTH,
                    level.speedParticles.get(i).yPos
                            + GameValues.SPEED_PARTICLE_HEIGHT);
        }

        c.set(0x42453aff);
//        renderer.circle(player.getXCenter(), player.getYCenter(),
//                LOW_F_HEIGHT * 1.15f);

        // PLATFORMS:
        for (int i = 0; i < level.platformsRight.size(); ++i) {
            c.set(0x6f6f6fff);
            drawTop = true;
            drawBottom = true;
            if (level.platformsRight.get(i).hasNext
                    || Game.state != GameState.Playing) {
                drawTop = false;
            }
            if (level.platformsRight.get(i).hasPrevious
                    || Game.state != GameState.Playing) {
                drawBottom = false;
            }

            Game.rectangle(renderer, level.platformsRight.get(i).xPos,
                    level.platformsRight.get(i).yPos,
                    GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                    false, true, drawTop, drawBottom);

            if (player.goingRight && alphaM > 0) {
                c.set(0xe5e4a0ff);
                c.a = alphaM / 255f;
                Game.rectangle(renderer, level.platformsRight.get(i).xPos,
                        level.platformsRight.get(i).yPos,
                        GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                        false, true, drawTop, drawBottom);
            }
        }
        for (int i = 0; i < level.platformsLeft.size(); ++i) {
            c.set(0x5b5b5bff);
            drawTop = true;
            drawBottom = true;
            if (level.platformsLeft.get(i).hasNext
                    || Game.state != GameState.Playing) {
                drawTop = false;
            }
            if (level.platformsLeft.get(i).hasPrevious
                    || Game.state != GameState.Playing) {
                drawBottom = false;
            }
            Game.rectangle(renderer, level.platformsLeft.get(i).xPos,
                    level.platformsLeft.get(i).yPos, GameValues.PLATFORM_WIDTH,
                    GameValues.PLATFORM_HEIGHT, true, false, drawTop,
                    drawBottom);
            if (!player.goingRight && alphaM > 0) {
                c.set(0xe5e4a0);
                c.a = alphaM / 255f;
                Game.rectangle(renderer, level.platformsLeft.get(i).xPos,
                        level.platformsLeft.get(i).yPos,
                        GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                        true, false, drawTop, drawBottom);
            }
        }

        // PAINT
        for (int i = 0; i < player.paintTrail.size(); ++i) {
            c.set(color);
            if (player.paintTrail.get(i).active) {

                renderer.rect(player.paintTrail.get(i).xPos,
                        player.paintTrail.get(i).yPos,
                        GameValues.PAINT_THICKNESS,
                        player.paintTrail.get(i).height);
            }

            if (player.paintTrail.get(i).isRight() != player.goingRight
                    && alphaM > 0) {
                c.set(0xe5e475);
                c.a = alphaM / 255f;
                renderer.rect(player.paintTrail.get(i).xPos,
                        player.paintTrail.get(i).yPos,
                        GameValues.PAINT_THICKNESS,
                        player.paintTrail.get(i).height);
            }
        }

        // SPLASH
        for (int i = 0; i < player.splashParticles1.size(); i++) {
            player.splashParticles1.get(i).draw(renderer);
        }
        for (int i = 0; i < player.splashParticles2.size(); i++) {
            player.splashParticles2.get(i).draw(renderer);
        }

        // PLAYER:
        player.draw(renderer);

        paintText.setTextSize(w / 11);
        for (int i = 0; i < animatedTexts.size(); ++i) {
            if (animatedTexts.get(i).active) {
                paintText.setAlpha(animatedTexts.get(i).alpha);
                renderer.drawText(animatedTexts.get(i).text,
                        animatedTexts.get(i).xPos, animatedTexts.get(i).yPos);
            }
        }

        // CIRCLES
        for (int i = 0; i < circles.size(); ++i) {
            if (circles.get(i).active) {
                c.set(0xe5e4a0ff);
                c.a = circles.get(i).a / 255f;
                renderer.circle(circles.get(i).xPos, circles.get(i).yPos,
                        circles.get(i).scale);
            }
        }

        if (state == GameState.Menu) {
            c.set(Color.WHITE);
            renderer.drawBitmap(BitmapLoader.leader, leaderBtn.xPos,
                    leaderBtn.yPos);
            renderer.drawBitmap(BitmapLoader.achiv, rateBtn.xPos, rateBtn.yPos);
            renderer.drawBitmap(BitmapLoader.store, storeBtn.xPos, storeBtn.yPos);
            renderer.drawBitmap(BitmapLoader.achievm, achievBtn.xPos,
                    achievBtn.yPos);
            renderer.drawBitmap(BitmapLoader.share, shareBtn.xPos, shareBtn.yPos);
            if (isMusicEnabled)
                renderer.drawBitmap(BitmapLoader.sound, soundBtn.xPos,
                        soundBtn.yPos);
            else
                renderer.drawBitmap(BitmapLoader.soundO, soundBtn.xPos,
                        soundBtn.yPos);

            if (mode == GameMode.Arcade)
                renderer.drawBitmap(BitmapLoader.modeArcade, modeBtn.xPos,
                        modeBtn.yPos);
            else if (mode == GameMode.Recruit)
                renderer.drawBitmap(BitmapLoader.modeRecruit, modeBtn.xPos,
                        modeBtn.yPos);
            else if (mode == GameMode.Ultra)
                renderer.drawBitmap(BitmapLoader.modeUltra, modeBtn.xPos,
                        modeBtn.yPos);
            else if (mode == GameMode.Singularity)
                renderer.drawBitmap(BitmapLoader.modeSingular, modeBtn.xPos,
                        modeBtn.yPos);
            else if (mode == GameMode.SpeedRunner)
                renderer.drawBitmap(BitmapLoader.modeSpeed, modeBtn.xPos,
                        modeBtn.yPos);

            // TEXT
            c.set(0xe5e4a0ff);

            paintText.setTextSize((w / 4.85f));
            paintText.getTextBounds("BASS", 0, "JUMP".length(), result);
            renderer.drawText("BASS", leaderBtn.xPos - GameValues.BUTTON_PADDING,
                    (result.height() * 1.25f));
            renderer.drawText("JUMP", leaderBtn.xPos - GameValues.BUTTON_PADDING,
                    (result.height() * 2.25f));
            paintText.setTextSize(w / 19.25f);
            renderer.drawText("Tap anywhere", leaderBtn.xPos
                    - GameValues.BUTTON_PADDING, (result.height() * 2.75f));
            renderer.drawText("to Start", leaderBtn.xPos
                    - GameValues.BUTTON_PADDING, (result.height() * 3.1f));

            // SONG NAME:
            c.set(0xe5e4a0ff);


            // COINS:
            txt = Utility.formatNumber(Utility.getCoins(con));
            renderer.drawText(txt, storeBtn.xPos - GameValues.BUTTON_PADDING,
                    storeBtn.yPos + GameValues.BUTTON_SCALE);
            paintText.getTextBounds(txt, 0, txt.length(), result);
            renderer.drawBitmap(
                    BitmapLoader.coin,
                    (storeBtn.xPos - result.width())
                            - (GameValues.COIN_SCALE + GameValues.BUTTON_PADDING * 1.225f),
                    (storeBtn.yPos + GameValues.BUTTON_SCALE)
                            - GameValues.COIN_SCALE);

            // SCORE & STATS:
            txt = ("Played: " + player.gamesPlayed);
            c.set(0xe5e4a0ff);

            paintText.setTextSize(w / 19.25f);
            paintText.getTextBounds(txt, 0, txt.length(), result);
            renderer.drawText(
                    txt,
                    (achievBtn.xPos + GameValues.BUTTON_SCALE + GameValues.BUTTON_PADDING),
                    (achievBtn.yPos + GameValues.BUTTON_SCALE));
            scoreText = ("Best: " + player.highScoreA);
            if (mode == GameMode.Recruit) {
                scoreText = ("Best: " + player.highScoreR);
            } else if (mode == GameMode.Ultra) {
                scoreText = ("Best: " + player.highScoreU);
            } else if (mode == GameMode.Singularity) {
                scoreText = ("Best: " + player.highScoreS);
            } else if (mode == GameMode.SpeedRunner) {
                scoreText = ("Best: " + player.highScoreS2);
            }

            renderer.drawText(
                    scoreText,
                    (achievBtn.xPos + GameValues.BUTTON_SCALE + GameValues.BUTTON_PADDING),
                    (achievBtn.yPos + GameValues.BUTTON_SCALE)
                            - result.height());

            // MODE:

            paintText.setTextSize(w / 27);
            txt = "Arcade";
            if (mode == GameMode.Recruit) {
                txt = "Recruit";
            } else if (mode == GameMode.Singularity) {
                txt = "Singularity";
            } else if (mode == GameMode.Ultra) {
                txt = "Ultra";
            } else if (mode == GameMode.SpeedRunner) {
                txt = "Runner";
            }
            renderer.drawText(txt, modeBtn.xPos + (GameValues.BUTTON_SCALE / 2),
                    (modeBtn.yPos + GameValues.BUTTON_SCALE)
                            + (GameValues.BUTTON_PADDING * 1.15f));
            // RANK:

        } else if (state == GameState.Playing) {
            // SCORE
            c.set(0xe5e4a0ff);
            paintText.setTextSize((w / 4.1f) * scoreTextMult);

            paintText.getTextBounds(String.valueOf(player.score), 0, String
                    .valueOf(player.score).length(), result);
            paintText.setAlpha(255);
            if (player.score > 0) {
                renderer.drawText(String.valueOf(player.score),
                        (w / 2),
                        scoreDisplay.yPos + (result.height() / 2));
            } else {
                renderer.drawText(String.valueOf(1), (w / 2),
                        scoreDisplay.yPos + (result.height() / 2));
            }
            paintText.getTextBounds("0", 0, "0".length(), result);
            paintText.setTextSize(w / 15.5f);
            txt = "";
            if (mode == GameMode.Arcade) {
                txt = ("BEST: " + String.valueOf(player.highScoreA));
                if (player.score > player.highScoreA)
                    txt = ("NEW BEST!");
            } else if (mode == GameMode.Recruit) {
                txt = ("BEST: " + String.valueOf(player.highScoreR));
                if (player.score > player.highScoreR)
                    txt = ("NEW BEST!");
            } else if (mode == GameMode.Ultra) {
                txt = ("BEST: " + String.valueOf(player.highScoreU));
                if (player.score > player.highScoreU)
                    txt = ("NEW BEST!");
            } else if (mode == GameMode.Singularity) {
                txt = ("BEST: " + String.valueOf(player.highScoreS));
                if (player.score > player.highScoreS)
                    txt = ("NEW BEST!");
            } else if (mode == GameMode.SpeedRunner) {
                txt = ("BEST: " + String.valueOf(player.highScoreS2));
                if (player.score > player.highScoreS2)
                    txt = ("NEW BEST!");
            }
            c.set(0xe5e4a0ff);
            renderer.drawText(txt, (w / 2), scoreDisplay.yPos
                    + (result.height()));
        }

        // INTRO
        if (introShowing) {
            c.set(0x3e3e3eff);
            renderer.rect(0, 0, w, h);
            c.set(0xe5e4a0ff);
            paintText.setTextSize(w / 9);

            renderer.drawText("The Big Shots", (w / 2),
                    (h / 2));
            paintText.setTextSize(w / 25);
            renderer.drawText("Thank you for Playing!", (w / 2),
                    h - GameValues.BUTTON_PADDING);
            c.set(0xe532cdff);
            renderer.rect((w / 2)
                            - (GameValues.LOADING_BAR_WIDTH / 2), h
                            - GameValues.LOADING_BAR_WIDTH / 2f,
                    ((w / 2) - (GameValues.LOADING_BAR_WIDTH / 2))
                            + loadWidth,
                    (h - GameValues.LOADING_BAR_WIDTH / 2f)
                            + GameValues.LOADING_BAR_WIDTH / 10);
        }
    }

    @Override
    public void dispose() {
        bitmapLoader.dispose();
        Utility.disposeFont();
    }

    public void drawHUD() {

    }
}
