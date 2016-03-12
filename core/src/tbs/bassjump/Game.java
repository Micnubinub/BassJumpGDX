package tbs.bassjump;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.ArrayList;
import java.util.Random;

import tbs.bassjump.animation.MovingText;
import tbs.bassjump.levels.Level;
import tbs.bassjump.managers.BitmapLoader;
import tbs.bassjump.objects.AnimCircle;
import tbs.bassjump.objects.Player;
import tbs.bassjump.ui.Dialog;
import tbs.bassjump.utility.GameObject;
import tbs.bassjump.view_lib.ValueAnimator;

public class Game extends ApplicationAdapter {
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
    //Todo make everything a texture >> walls > 1x1 opaque, 'rain particles' 1x1 translucent ** group with sprites.png
    //Todo make order of drawing 1) walls, player, rain particle 2)menu, text >> group textures
    // PAINTER:
    private static final Color c = new Color();
    //MusicShuffle
    private static final Random random = new Random();
    public static int[] colors = new int[]{0xffbb00ff};
    // CONTEXT
    // LEVEL AND PLAYER:
    public static Player player; // PLAYER CONTAINS PLAYER INFO
    public static Level level; // LEVEL CONTAINS LEVEL OBJECTS
    // MUSIC
    public static int alphaM;
    public static boolean isMusicEnabled;
    // STATE
    public static GameState state = GameState.Menu;
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
    public static boolean showAds;
    //    private static final ArrayList<ValueAnimator> animations = new ArrayList<ValueAnimator>(10);
    public static int w, h;
    public static SpriteBatch spriteBatch;
    public static short delta;
    public static Dialog shop;
    public static OrthographicCamera camera;
    private static String currSong;
    // MOVING TEXTS:
    private static ArrayList<MovingText> animatedTexts; // ANIMATED TEXT LIST
    private static int animatedTextIndex; // WHICH TEXT TO USE
    // INTERFACE:
    private static GameObject scoreDisplay;
    // GLOBAL PARTICLES:
    private static ArrayList<AnimCircle> circles;
    private static int circleIndex;
    private static ArrayList<ShaderProgram> shaderPrograms;
    private static ArrayList<ValueAnimator> animators = new ArrayList<ValueAnimator>();
    // ANIMATION
//    private static String songName;
    // INTRO
    private static int introDelay;
    private static int loadProg;
    private static int loadWidth;
    // RANKING:
//    private static LeaderboardScore leaderboard;
    private static BitmapLoader bitmapLoader;


    public static void initDisposables() {
        spriteBatch = new SpriteBatch();
        bitmapLoader = new BitmapLoader();
        ambientMusic = Gdx.audio.newMusic(Gdx.files.internal("song1.mp3"));
        ambientMusic.setLooping(true);
    }

    public static void addAnimator(ValueAnimator animator) {
        if (animator == null)
            return;

        if (!animators.contains(animator))
            try {
                animators.add(animator);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void removeAnimator(ValueAnimator animator) {
        if (animator == null)
            return;

        if (animators.contains(animator))
            try {
                animators.remove(animator);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void setup() {
        // MUSIC
        alphaM = 0;
        // MENU
        // menuTextAlpha = 255;

        // SPEED CALC
        GameValues.SPEED_FACTOR_ORIGINAL = ((float) h / 600);

        player = new Player();

        String shape = Utility.getString(
                Utility.EQUIPPED_SHAPE);
        if (shape == null || shape.length() < 2)
            shape = Utility.SHAPE_RECTANGLE;
        Player.setPlayerShape(Utility.getShapeType(shape));

        level = new Level();
        setupGame();

        setupInterface();
//        Log.e("setUp ticToc = ", String.valueOf(System.currentTimeMillis() - tic));
    }

    protected static void clear() {
        Gdx.gl.glClearColor(.22f, .22f, .22f, 1);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
    }

    public static void setupGame() {
        // SETUP NEW GAME
        // ADS

        if (showAds) {
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

    private static void rectangle(int x, int y, int w,
                                  int h, boolean drawLeft, boolean drawRight, boolean drawTop,
                                  boolean drawBottom) {
        //Test
        if (drawLeft)
            renderer.rect(x, y, GameValues.STROKE_WIDTH, h);

        if (drawRight)
            renderer.rect(x + w, y, GameValues.STROKE_WIDTH, h);

        if (drawTop)
            renderer.rect(x, y, w, GameValues.STROKE_WIDTH);

        if (drawBottom)
            renderer.rect(x, (y + h), w, GameValues.STROKE_WIDTH);
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

    public static void showCircle(int x, int y) {
        circleIndex += 1;
        if (circleIndex == circles.size())
            circleIndex = 0;
        circles.get(circleIndex).activate(x, y);
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
        try {
            ambientMusic.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playMusic() {
        try {
            ambientMusic.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void beginSpriteBatch() {

        if (!spriteBatch.isDrawing())
            spriteBatch.begin();
    }

    @Override
    public void create() {
        initDisposables();
        init();
    }

    public void update() {
        delta = (short) (Gdx.graphics.getDeltaTime() * 1000);
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
        camera.translate(w / 2, h / 2);
        camera.setToOrtho(true);
        // ACHIEVEMENT:
//        unlockAchievement("CgkIvYbi1pMMEAIQDA");

        // SETUP:
        setup();
        // LOAD AD:
// Todo       adManager.loadFullscreenAd();
        long tic = System.currentTimeMillis();

        // LOAD DATA:
        showAds = Utility.getString("nerUds") == null;

        if (Utility.getString("hScore") != null) {
            player.highScoreA = Utility.getInt("hScore");
        } else {
            player.highScoreA = 0;
        }
        if (Utility.getString("hScoreR") != null) {
            player.highScoreR = Utility.getInt("hScoreR");
        } else {
            player.highScoreR = 0;
        }
        if (Utility.getString("hScoreU") != null) {
            player.highScoreU = Utility.getInt("hScoreU");
        } else {
            player.highScoreR = 0;
        }
        if (Utility.getString("hScoreS") != null) {
            player.highScoreS = Utility.getInt("hScoreS");
        } else {
            player.highScoreS = 0;
        }
        if (Utility.getString("hScoreS2") != null) {
            player.highScoreS2 = Utility.getInt("hScoreS2");
        } else {
            player.highScoreS2 = 0;
        }

        if (Utility.getString("musicOn") != null) {
            if (Utility.getString("musicOn").equals("off")) {
                isMusicEnabled = false;
                pauseMusic();
            } else {
                playMusic();
                isMusicEnabled = true;
            }
        } else {
            isMusicEnabled = true;
        }

        mode = GameMode.Arcade;
        if (Utility.getString("gMode") != null) {
            if (Utility.getString("gMode").equals("arcade")) {
                mode = GameMode.Arcade;
            } else if (Utility.getString("gMode").equals("recruit")) {
                mode = GameMode.Recruit;
            } else if (Utility.getString("gMode").equals("ultra")) {
                mode = GameMode.Ultra;
            } else if (Utility.getString("gMode").equals("singul")) {
                mode = GameMode.Singularity;
            } else if (Utility.getString("gMode").equals("speed")) {
                mode = GameMode.SpeedRunner;
                GameValues.PLAYER_JUMP_SPEED_MULT = 8;
            }
        }

        if (Utility.getString("gPlayed") != null) {
            player.gamesPlayed = Utility.getInt("gPlayed") + 1;
        } else {
            player.gamesPlayed = 0;
        }


        circles = new ArrayList<AnimCircle>();
        circleIndex = 0;
        for (int i = 0; i < 2; ++i) {
            circles.add(new AnimCircle());
        }

        // LOAD IMAGES ONCE


        introDelay = 150;
        loadProg = 0;
        loadWidth = 0;
        introShowing = true;
//  Todo      adManager = new AdManager(con);
//   Todo     Utility.addGameColors();

    }

    @Override
    public void render() {
        clear();
        camera.update();
        update();
        onDraw();
    }

    public void onDraw() {
        // DRAW EVERYTHING IN ORDER:
        // c.set(0x000000); // DEFAULT

        setColor(0xffffff55);
        for (int i = 0; i < level.speedParticles.size(); ++i) {
            renderer.rect(level.speedParticles.get(i).xPos,
                    level.speedParticles.get(i).yPos,
                    GameValues.SPEED_PARTICLE_WIDTH,
                    GameValues.SPEED_PARTICLE_HEIGHT);
        }

//        c.set(0x42453aff);
//        renderer.setColor(c);
//        renderer.circle(player.getXCenter(), player.getYCenter(),
//                LOW_F_HEIGHT * 1.15f);

        // PLATFORMS:
        setColor(0x6f6f6fff);
        for (int i = 0; i < level.platformsRight.size(); ++i) {
            drawTop = true;
            drawBottom = true;
            if (level.platformsRight.get(i).hasNext
                    || state != GameState.Playing) {
                drawTop = false;
            }
            if (level.platformsRight.get(i).hasPrevious
                    || state != GameState.Playing) {
                drawBottom = false;
            }

            rectangle(level.platformsRight.get(i).xPos,
                    level.platformsRight.get(i).yPos,
                    GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                    false, true, drawTop, drawBottom);

            if (player.goingRight && alphaM > 0) {
                c.set(0xe5e4a0ff);
                c.a = alphaM / 255f;
                renderer.setColor(c);
                rectangle(level.platformsRight.get(i).xPos,
                        level.platformsRight.get(i).yPos,
                        GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                        false, true, drawTop, drawBottom);
            }
        }

        setColor(0x5b5b5bff);

        for (int i = 0; i < level.platformsLeft.size(); ++i) {
            drawTop = true;
            drawBottom = true;
            if (level.platformsLeft.get(i).hasNext
                    || state != GameState.Playing) {
                drawTop = false;
            }
            if (level.platformsLeft.get(i).hasPrevious
                    || state != GameState.Playing) {
                drawBottom = false;
            }
            rectangle(level.platformsLeft.get(i).xPos,
                    level.platformsLeft.get(i).yPos, GameValues.PLATFORM_WIDTH,
                    GameValues.PLATFORM_HEIGHT, true, false, drawTop,
                    drawBottom);
            if (!player.goingRight && alphaM > 0) {
                c.set(0xe5e4a0ff);
                c.a = alphaM / 255f;
                renderer.setColor(c);
                rectangle(level.platformsLeft.get(i).xPos,
                        level.platformsLeft.get(i).yPos,
                        GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                        true, false, drawTop, drawBottom);
            }
        }


        // PAINT
        for (int i = 0; i < player.paintTrail.size(); ++i) {
            setColor(color);
            if (player.paintTrail.get(i).active) {
                renderer.rect(player.paintTrail.get(i).xPos,
                        player.paintTrail.get(i).yPos,
                        GameValues.PAINT_THICKNESS,
                        player.paintTrail.get(i).height);
            }

            if (player.paintTrail.get(i).isRight() != player.goingRight
                    && alphaM > 0) {
                c.set(0xe5e475ff);
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
        beginSpriteBatch();
        player.draw(spriteBatch);
        for (int i = 0; i < animatedTexts.size(); ++i) {
            if (animatedTexts.get(i).active) {
                c.a = animatedTexts.get(i).alpha / 255f;
                Utility.drawCenteredText(spriteBatch, c, animatedTexts.get(i).text,
                        animatedTexts.get(i).xPos, h - animatedTexts.get(i).yPos, Utility.getScale(w / 11));
            }
        }


        // CIRCLES
        for (int i = 0; i < circles.size(); ++i) {
            if (circles.get(i).active) {
                c.set(0xffffffff);
                c.a = circles.get(i).a;
                renderer.setColor(c);
                renderer.circle(circles.get(i).xPos, circles.get(i).yPos,
                        circles.get(i).scale);
            }
        }

        if (state == GameState.Menu) {
            c.set(Color.WHITE);
            beginSpriteBatch();
            spriteBatch.draw(BitmapLoader.leader, leaderBtn.xPos,
                    h - leaderBtn.yPos - leaderBtn.scale, leaderBtn.scale, leaderBtn.scale);
            spriteBatch.draw(BitmapLoader.achiv, rateBtn.xPos, h - rateBtn.yPos - rateBtn.scale, rateBtn.scale, rateBtn.scale);
            spriteBatch.draw(BitmapLoader.store, storeBtn.xPos, h - storeBtn.yPos - storeBtn.scale, storeBtn.scale, storeBtn.scale);
            spriteBatch.draw(BitmapLoader.achievm, achievBtn.xPos,
                    h - achievBtn.yPos - achievBtn.scale, achievBtn.scale, achievBtn.scale);
            spriteBatch.draw(BitmapLoader.share, shareBtn.xPos, h - shareBtn.yPos - shareBtn.scale, shareBtn.scale, shareBtn.scale);
            spriteBatch.draw(isMusicEnabled ? BitmapLoader.sound : BitmapLoader.soundO, soundBtn.xPos,
                    h - soundBtn.yPos - soundBtn.scale, soundBtn.scale, soundBtn.scale);

            if (mode == GameMode.Arcade)
                spriteBatch.draw(BitmapLoader.modeArcade, modeBtn.xPos,
                        h - modeBtn.yPos - modeBtn.scale, modeBtn.scale, modeBtn.scale);
            else if (mode == GameMode.Recruit)
                spriteBatch.draw(BitmapLoader.modeRecruit, modeBtn.xPos,
                        h - modeBtn.yPos - modeBtn.scale, modeBtn.scale, modeBtn.scale);
            else if (mode == GameMode.Ultra)
                spriteBatch.draw(BitmapLoader.modeUltra, modeBtn.xPos,
                        h - modeBtn.yPos - modeBtn.scale, modeBtn.scale, modeBtn.scale);
            else if (mode == GameMode.Singularity)
                spriteBatch.draw(BitmapLoader.modeSingular, modeBtn.xPos,
                        h - modeBtn.yPos - modeBtn.scale, modeBtn.scale, modeBtn.scale);
            else
                spriteBatch.draw(BitmapLoader.modeSpeed, modeBtn.xPos,
                        h - modeBtn.yPos - modeBtn.scale, modeBtn.scale, modeBtn.scale);

            // TEXT
            c.set(0xe5e4a0ff);

            float textH = w / 4.5f;
            float[] textSize = Utility.measureText("BASS", Utility.getScale(textH));
            Utility.drawCenteredText(spriteBatch, c, "BASS", (w / 2) - GameValues.BUTTON_PADDING,
                    h - (textSize[1] * 1.25f), Utility.getScale(textH));
            Utility.drawCenteredText(spriteBatch, c, "JUMP", (w / 2) - GameValues.BUTTON_PADDING,
                    h - (textSize[1] * 2.25f) - GameValues.BUTTON_PADDING, Utility.getScale(textH));

            textH = w / 15f;

            Utility.drawCenteredText(spriteBatch, c, "Tap anywhere to start", w / 2, h / 2, Utility.getScale(textH));


            c.set(0xffffffff);
            // COINS:

            txt = Utility.formatNumber(Utility.getCoins());
            textSize = Utility.measureText(txt, 0.3f);

            final float coinsY = h - storeBtn.yPos - storeBtn.scale + (textSize[1] / 2);
            Utility.drawCenteredText(spriteBatch, c, txt, storeBtn.xPos - GameValues.BUTTON_PADDING - (textSize[0] / 2),
                    coinsY, 0.3f);

            spriteBatch.draw(BitmapLoader.coin, (storeBtn.xPos - w / 8)
                            - (GameValues.COIN_SCALE + GameValues.BUTTON_PADDING * 1.45f),
                    coinsY - (GameValues.COIN_SCALE * 0.7f), GameValues.COIN_SCALE * 2, GameValues.COIN_SCALE * 2);


            // SCORE & STATS:
            txt = ("Played: " + player.gamesPlayed);

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

            c.set(0xe5e4a0ff);

            textSize = Utility.measureText(scoreText, 0.3f);

            final float left = achievBtn.xPos + GameValues.BUTTON_SCALE + GameValues.BUTTON_PADDING;
            Utility.drawLeftText(spriteBatch, c, txt, left, h - achievBtn.yPos - GameValues.BUTTON_SCALE + textSize[1] + (GameValues.BUTTON_PADDING), 0.3f);
            Utility.drawLeftText(spriteBatch, c, scoreText, left, h - achievBtn.yPos - GameValues.BUTTON_SCALE, 0.3f);

            // MODE:
            if (mode == GameMode.Recruit) {
                txt = "Recruit";
            } else if (mode == GameMode.Singularity) {
                txt = "Singularity";
            } else if (mode == GameMode.Ultra) {
                txt = "Ultra";
            } else if (mode == GameMode.SpeedRunner) {
                txt = "Runner";
            } else {
                txt = "Arcade";
            }
            Utility.drawCenteredText(spriteBatch, c, txt, modeBtn.xPos + (GameValues.BUTTON_SCALE / 2),
                    h - ((modeBtn.yPos + GameValues.BUTTON_SCALE)
                            + (GameValues.BUTTON_PADDING * 1.15f)), Utility.getScale(w / 27));
            // RANK:
        } else if (state == GameState.Playing) {
            // SCORE
            beginSpriteBatch();
            c.set(0xe5e4a0ff);
            final float scale = Utility.getScale((w / 4.1f) * scoreTextMult);
            float[] scoreTextSize = Utility.measureText("1", scale);
            if (player.score > 0) {
                Utility.drawCenteredText(spriteBatch, c, String.valueOf(player.score),
                        (w / 2), h - scoreDisplay.yPos, scale);
            } else {
                Utility.drawCenteredText(spriteBatch, c, "1", (w / 2),
                        h - scoreDisplay.yPos, scale);
            }
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

            txt = txt + " fps: " + Gdx.graphics.getFramesPerSecond();
            c.set(0xe5e4a0ff);
            Utility.drawCenteredText(spriteBatch, c, txt, (w / 2), h - scoreDisplay.yPos - scoreTextSize[1], Utility.getScale(w / 15.5f));
        }

        // INTRO
        if (introShowing) {

            setColor(0x3e3e3eff);
            renderer.rect(0, 0, w, h);
            c.set(0xe5e4a0ff);

            beginSpriteBatch();
            Utility.drawCenteredText(spriteBatch, c, "The Big Shots", (w / 2),
                    h - (h / 2), Utility.getScale(w / 8));
            Utility.drawCenteredText(spriteBatch, c, "Thank you for Playing!", (w / 2),
                    h - (h - GameValues.BUTTON_PADDING - GameValues.BUTTON_PADDING), Utility.getScale(w / 20));


            setColor(0xe532cdff);

            renderer.rect((w / 2) - (GameValues.LOADING_BAR_WIDTH / 2),
                    h - GameValues.LOADING_BAR_WIDTH / 2f,
                    loadWidth, GameValues.LOADING_BAR_WIDTH / 10);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        try {
            bitmapLoader.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ambientMusic.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            spriteBatch.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (shaderPrograms != null) {
            for (ShaderProgram shaderProgram : shaderPrograms) {
                Utility.dispose(shaderProgram);
            }
        }

        Utility.disposeFont();
    }

    @Override
    public void resume() {
        super.resume();

        System.out.println("resume");
        initDisposables();
    }


}
