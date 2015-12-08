package tbs.bassjump;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import tbs.bassjump.animation.MovingText;
import tbs.bassjump.levels.Level;
import tbs.bassjump.managers.BitmapLoader;
import tbs.bassjump.objects.AnimCircle;
import tbs.bassjump.objects.Player;
import tbs.bassjump.utility.AdManager;
import tbs.bassjump.utility.GameObject;


public class Game extends Screen {
    // PAINTER:
    private static final Color c = new Color();
    private static final Rect paintTrailRect = new Rect();
    //MusicShuffle
    private static final Random random = new Random();
    public static int[] colors = new int[]{0xffbb00};
    // CONTEXT
    // LEVEL AND PLAYER:
    public static Player player; // PLAYER CONTAINS PLAYER INFO
    public static Level level; // LEVEL CONTAINS LEVEL OBJECTS
    // MUSIC
    public static int alphaM;
    public static boolean isMusicEnabled;
    // STATE:
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
    public static AdManager adManager;
    // CANVAS DATA;
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
    private static String currSong;
    // MOVING TEXTS:
    private static ArrayList<MovingText> animatedTexts; // ANIMATED TEXT LIST
    private static int animatedTextIndex; // WHICH TEXT TO USE
    // INTERFACE:
    private static GameObject scoreDisplay;
    // GLOBAL PARTICLES:
    private static ArrayList<AnimCircle> circles;
    private static int circleIndex;
    // ANIMATION
    private static String songName;
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
        GameValues.SPEED_FACTOR_ORIGINAL = ((float) ScreenDimen.height / 600);

//        Utility.log("SPEED: " + GameValues.SPEED_FACTOR_ORIGINAL);

        player = new Player();

        String shape = Utility.getPrefs(context).getString(
                Utility.EQUIPPED_SHAPE);
        if (shape == null || shape.length() < 2)
            shape = Utility.SHAPE_RECTANGLE;
        Player.setPlayerShape(Utility.getShapeType(shape));

        level = new Level();
        setupGame();

        setupInterface();
//        Log.e("setUp ticToc = ", String.valueOf(System.currentTimeMillis() - tic));
    }

    public void update() {
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

    public static void setupGame() {
        // SETUP NEW GAME
        // ADS

        if (MainActivity.showAds) {
            MainActivity.getView().post(new Runnable() { // LOAD AD
                @Override
                public void run() {
                    if (!Game.adManager.getFullscreenAd().isLoaded())
                        Game.adManager.loadFullscreenAd();
                }
            });
            if ((player.gamesPlayed + 1) % 10 == 0 && player.gamesPlayed > 0) {
                // AD WARNING:
                MainActivity.getView().post(new Runnable() {
                    @Override
                    public void run() {
                        Utility.showToast("Ad Showing!", context);
                    }
                });
            }

            if (player.gamesPlayed % 10 == 0 && player.gamesPlayed > 0) {
                MainActivity.getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final InterstitialAd ad = Game.adManager
                                .getFullscreenAd();
                        if (ad.isLoaded()) {
                            ad.show();
                        }
                    }
                });

            }
        }
        Utility.log("tbs.bassjump.reference.Game Setup Initialized");
        GameValues.SPEED_BONUS = 1;

        // COLORS:
        color = colors[Utility.randInt(0, colors.length - 1)];

        // SAVE SCORE TO LB
        if (MainActivity.getApiClient().isConnected()) {
            if (player.highScoreA > 0)
                Games.Leaderboards.submitScore(MainActivity.getApiClient(),
                        "CgkIvYbi1pMMEAIQBg", player.highScoreA);
            if (player.highScoreR > 0)
                Games.Leaderboards.submitScore(MainActivity.getApiClient(),
                        "CgkIvYbi1pMMEAIQBw", player.highScoreR);
            if (player.highScoreU > 0)
                Games.Leaderboards.submitScore(MainActivity.getApiClient(),
                        "CgkIvYbi1pMMEAIQEQ", player.highScoreU);
            if (player.highScoreS > 0)
                Games.Leaderboards.submitScore(MainActivity.getApiClient(),
                        "CgkIvYbi1pMMEAIQEg", player.highScoreS);
            if (player.highScoreS2 > 0)
                Games.Leaderboards.submitScore(MainActivity.getApiClient(),
                        "CgkIvYbi1pMMEAIQFA", player.highScoreS);

        }

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

    private static void drawRectangle(Canvas canvas, int x, int y, int w,
                                      int h, boolean drawLeft, boolean drawRight, boolean drawTop,
                                      boolean drawBottom) {
        if (drawLeft)
            canvas.drawLine(x, y, x, y + h, paint);

        if (drawRight)
            canvas.drawLine(x + w, y, x + w, y + h, paint);

        if (drawTop)
            canvas.drawLine(x, y, x + w, y, paint);

        if (drawBottom)
            canvas.drawLine(x, y + h, x + w, y + h, paint);

    }

    private static void checkAchievements() {
        // SCORE RELATED:
        if (MainActivity.getApiClient().isConnected()) {
            if (player.highScoreA >= 10)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQAQ");
            if (player.highScoreA >= 50)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQCA");
            if (player.highScoreA >= 100)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQCg");
            if (player.highScoreA >= 200)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQDQ");
            if (player.highScoreR >= 10)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQAg");
            if (player.highScoreR >= 50)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQCQ");

            // DEATH & GAMES RELATED:
            if (player.gamesPlayed >= 1000)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQAw");
            if (player.gamesPlayed >= 500)
                MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQBA");

            // OTHER
            MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQDA");
        }
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
        scoreDisplay.xPos = ScreenDimen.getCenterX();
        scoreDisplay.scale = ScreenDimen.height / 13;
        scoreDisplay.scale2 = (int) (scoreDisplay.scale * 1.25f);
        scoreDisplay.yPos = (int) (scoreDisplay.scale * 1.35f);

        // BUTTONS:
        leaderBtn = new GameObject();
        leaderBtn.scale = GameValues.BUTTON_SCALE;
        leaderBtn.xPos = (ScreenDimen.width - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        leaderBtn.yPos = GameValues.BUTTON_PADDING;

        rateBtn = new GameObject();
        rateBtn.scale = GameValues.BUTTON_SCALE;
        rateBtn.xPos = (ScreenDimen.width - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        rateBtn.yPos = GameValues.BUTTON_SCALE
                + (GameValues.BUTTON_PADDING * 2);

        modeBtn = new GameObject();
        modeBtn.scale = GameValues.BUTTON_SCALE;
        modeBtn.xPos = (ScreenDimen.width - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        modeBtn.yPos = (GameValues.BUTTON_SCALE * 2)
                + (GameValues.BUTTON_PADDING * 3);

        achievBtn = new GameObject();
        achievBtn.scale = GameValues.BUTTON_SCALE;
        achievBtn.xPos = GameValues.BUTTON_PADDING;
        achievBtn.yPos = modeBtn.yPos;

        soundBtn = new GameObject();
        soundBtn.scale = GameValues.BUTTON_SCALE;
        soundBtn.xPos = (ScreenDimen.width - GameValues.BUTTON_SCALE)
                - GameValues.BUTTON_PADDING;
        soundBtn.yPos = (ScreenDimen.height - GameValues.BUTTON_SCALE)
                - (GameValues.BUTTON_PADDING);

        storeBtn = new GameObject();
        storeBtn.scale = GameValues.BUTTON_SCALE;
        storeBtn.xPos = (ScreenDimen.width - GameValues.BUTTON_SCALE)
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

    public static void playSong(String file) {
        playSong(new File(file));
    }

    public static void pauseSong() {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public static void playSong() {
        if (!isMusicEnabled)
            return;
        if (mediaPlayer != null) {
            try {
                if (prepared)
                    mediaPlayer.start();
                else if (!prepared && ((mediaPlayer == null) || !(mediaPlayer.isPlaying()))) {
                    playNextSong();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void playSong(File file) {
        if (!isMusicEnabled)
            return;
        currSong = file.getAbsolutePath();
        playSong(Uri.fromFile(file));
    }

    private static void playSong(Uri uri) {
        release();
        if (!Game.isMusicEnabled) {
            return;
        }
        numberOfPlayNextSongRetries++;
        if (numberOfPlayNextSongRetries > 15) {
            return;
        }
        if (uri.toString().toLowerCase().contains("android.resource")) {
            mediaPlayer = MediaPlayer.create(context, R.raw.song1);
            prepared = true;
            mediaPlayer.start();
            numberOfPlayNextSongRetries = 0;
        } else {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            log("step2");
            try {
                mediaPlayer.setDataSource(context, uri);
                log("step3");
                try {
                    mediaPlayer.prepare();

                    log("step4");
                } catch (IOException e) {
                    log(e.toString());
                    log("step4 failed IO > " + uri.toString());
                    e.printStackTrace();
                    playNextSong();
                } catch (Exception e) {
                    log(e.toString());
                    e.printStackTrace();
                    log("step4 failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
                log("step3 failed");
            }
            mediaPlayer.setOnPreparedListener(prepareAndPlay);
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextSong();
            }
        });
        setUpSongDetails();
    }

    public static void playDefaultSong() {
        // playSong(Uri.parse("android.resource://" + context.getApplicationInfo().packageName + "/raw/song1"));
    }

    private static void setUpSongDetails() {
        final String[] songDetails = Utility.getSongTitle(currSong).split(Utility.SEP);
        if (songDetails == null
                || (songDetails[0] + songDetails[1]).length() < 2)
            songName = "Meizong - Colossus"; // DEFAULT
        else
            songName = songDetails[1] + " - " + songDetails[0];
    }

    public static void playNextSong() {
        if (!isMusicEnabled || songs == null) {
            return;
        }
        final int songIndex = random.nextInt(songs.length) % songs.length;
        playSong(songs[songIndex]);
    }


    public static void log(String log) {
        Gdx.app.log("game", log);
    }


    @Override
    public void init() {
// CONST
        log("initCalled");
        long tic = System.currentTimeMillis();
        ambientMusic = Gdx.audio.newMusic(Gdx.files.internal("ambient.mp3"));
        ambientMusic.setLooping(true);
        ambientMusic.play();

        circles = new ArrayList<AnimCircle>();
        circleIndex = 0;
        for (int i = 0; i < 2; ++i) {
            circles.add(new AnimCircle());
        }


        playNextSong();

        // LOAD IMAGES ONCE
        bitmapLoader = new BitmapLoader();

        introDelay = 150;
        loadProg = 0;
        loadWidth = 0;
        introShowing = true;
//  Todo      adManager = new AdManager(context);
//   Todo     Utility.addGameColors();

    }

    @Override
    public void onDraw() {
        // DRAW EVERYTHING IN ORDER:
        // paint.setColor(0x000000); // DEFAULT
        paint.setColor(0xffffffff); // FUCK ALPHA
        paint.setAlpha(5);
        for (int i = 0; i < level.speedParticles.size(); ++i) {
            canvas.drawRect(level.speedParticles.get(i).xPos,
                    level.speedParticles.get(i).yPos,
                    level.speedParticles.get(i).xPos
                            + GameValues.SPEED_PARTICLE_WIDTH,
                    level.speedParticles.get(i).yPos
                            + GameValues.SPEED_PARTICLE_HEIGHT, paint);
        }

        paint.setColor(0xff42453a);
//        canvas.drawCircle(player.getXCenter(), player.getYCenter(),
//                LOW_F_HEIGHT * 1.15f, paint);

        // PLATFORMS:
        for (int i = 0; i < level.platformsRight.size(); ++i) {
            paint.setColor(0xff6f6f6f);
            paint.setAlpha(255);
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

            Game.drawRectangle(canvas, level.platformsRight.get(i).xPos,
                    level.platformsRight.get(i).yPos,
                    GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                    false, true, drawTop, drawBottom);

            if (player.goingRight && alphaM > 0) {
                paint.setColor(0xffe5e4a0);
                paint.setAlpha(alphaM);
                Game.drawRectangle(canvas, level.platformsRight.get(i).xPos,
                        level.platformsRight.get(i).yPos,
                        GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                        false, true, drawTop, drawBottom);
            }
        }
        for (int i = 0; i < level.platformsLeft.size(); ++i) {
            paint.setColor(0xff5b5b5b);
            paint.setAlpha(255);
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
            Game.drawRectangle(canvas, level.platformsLeft.get(i).xPos,
                    level.platformsLeft.get(i).yPos, GameValues.PLATFORM_WIDTH,
                    GameValues.PLATFORM_HEIGHT, true, false, drawTop,
                    drawBottom);
            if (!player.goingRight && alphaM > 0) {
                paint.setColor(0xffe5e4a0);
                paint.setAlpha(alphaM);
                Game.drawRectangle(canvas, level.platformsLeft.get(i).xPos,
                        level.platformsLeft.get(i).yPos,
                        GameValues.PLATFORM_WIDTH, GameValues.PLATFORM_HEIGHT,
                        true, false, drawTop, drawBottom);
            }
        }

        // PAINT
        for (int i = 0; i < player.paintTrail.size(); ++i) {
            paint.setColor(color);
            paint.setAlpha(255);
            if (player.paintTrail.get(i).active) {
                paintTrailRect.set(
                        player.paintTrail.get(i).xPos,
                        player.paintTrail.get(i).yPos,
                        player.paintTrail.get(i).xPos
                                + GameValues.PAINT_THICKNESS,
                        player.paintTrail.get(i).yPos
                                + player.paintTrail.get(i).height);
                canvas.drawRoundRect(paintTrailRect, 8, 8, paint);
            }

            if (player.paintTrail.get(i).isRight() != player.goingRight
                    && alphaM > 0) {
                paint.setColor(0xffe5e475);
                paint.setAlpha(alphaM);
                paintTrailRect.set(
                        player.paintTrail.get(i).xPos,
                        player.paintTrail.get(i).yPos,
                        player.paintTrail.get(i).xPos
                                + GameValues.PAINT_THICKNESS,
                        player.paintTrail.get(i).yPos
                                + player.paintTrail.get(i).height);
                canvas.drawRoundRect(paintTrailRect, 8, 8, paint);
            }
        }

        // SPLASH
        for (int i = 0; i < player.splashParticles1.size(); i++) {
            player.splashParticles1.get(i).draw(canvas);
        }
        for (int i = 0; i < player.splashParticles2.size(); i++) {
            player.splashParticles2.get(i).draw(canvas);
        }

        // PLAYER:
        player.draw(canvas);

        paintText.setTextAlign(Align.CENTER);
        paintText.setTextSize(ScreenDimen.width / 11);
        for (int i = 0; i < animatedTexts.size(); ++i) {
            if (animatedTexts.get(i).active) {
                paintText.setAlpha(animatedTexts.get(i).alpha);
                canvas.drawText(animatedTexts.get(i).text,
                        animatedTexts.get(i).xPos, animatedTexts.get(i).yPos,
                        paintText);
            }
        }

        // CIRCLES
        for (int i = 0; i < circles.size(); ++i) {
            if (circles.get(i).active) {
                paint.setColor(0xffe5e4a0);
                paint.setAlpha(circles.get(i).a);
                canvas.drawCircle(circles.get(i).xPos, circles.get(i).yPos,
                        circles.get(i).scale, paint);
            }
        }

        paint.setAlpha(255);
        if (state == GameState.Menu) {
            paint.setColor(Color.WHITE);
            canvas.drawBitmap(BitmapLoader.leader, leaderBtn.xPos,
                    leaderBtn.yPos, paint);
            canvas.drawBitmap(BitmapLoader.achiv, rateBtn.xPos, rateBtn.yPos,
                    paint);
            canvas.drawBitmap(BitmapLoader.store, storeBtn.xPos, storeBtn.yPos,
                    paint);
            canvas.drawBitmap(BitmapLoader.achievm, achievBtn.xPos,
                    achievBtn.yPos, paint);
            canvas.drawBitmap(BitmapLoader.share, shareBtn.xPos, shareBtn.yPos,
                    paint);
            if (isMusicEnabled)
                canvas.drawBitmap(BitmapLoader.sound, soundBtn.xPos,
                        soundBtn.yPos, paint);
            else
                canvas.drawBitmap(BitmapLoader.soundO, soundBtn.xPos,
                        soundBtn.yPos, paint);

            if (mode == GameMode.Arcade)
                canvas.drawBitmap(BitmapLoader.modeArcade, modeBtn.xPos,
                        modeBtn.yPos, paint);
            else if (mode == GameMode.Recruit)
                canvas.drawBitmap(BitmapLoader.modeRecruit, modeBtn.xPos,
                        modeBtn.yPos, paint);
            else if (mode == GameMode.Ultra)
                canvas.drawBitmap(BitmapLoader.modeUltra, modeBtn.xPos,
                        modeBtn.yPos, paint);
            else if (mode == GameMode.Singularity)
                canvas.drawBitmap(BitmapLoader.modeSingular, modeBtn.xPos,
                        modeBtn.yPos, paint);
            else if (mode == GameMode.SpeedRunner)
                canvas.drawBitmap(BitmapLoader.modeSpeed, modeBtn.xPos,
                        modeBtn.yPos, paint);

            // TEXT
            paintText.setColor(0xffe5e4a0);
            paintText.setTextAlign(Align.RIGHT);
            paintText.setTextSize((ScreenDimen.width / 4.85f));
            paintText.getTextBounds("BASS", 0, "JUMP".length(), result);
            canvas.drawText("BASS", leaderBtn.xPos - GameValues.BUTTON_PADDING,
                    (result.height() * 1.25f), paintText);
            canvas.drawText("JUMP", leaderBtn.xPos - GameValues.BUTTON_PADDING,
                    (result.height() * 2.25f), paintText);
            paintText.setTextSize(ScreenDimen.width / 19.25f);
            canvas.drawText("Tap anywhere", leaderBtn.xPos
                            - GameValues.BUTTON_PADDING, (result.height() * 2.75f),
                    paintText);
            canvas.drawText("to Start", leaderBtn.xPos
                            - GameValues.BUTTON_PADDING, (result.height() * 3.1f),
                    paintText);

            // SONG NAME:
            paintText.setColor(0xffe5e4a0);
            paintText.setAlpha(255);
            paintText.setTextSize(ScreenDimen.width / 24);
            paintText.setTextAlign(Align.RIGHT);
            songName = (songName == null) ? "Music off" : songName;
            paintText.getTextBounds(songName, 0, songName.length(), result);

            if (isMusicEnabled && songName != null) {
                canvas.drawText(songName, soundBtn.xPos
                        - GameValues.BUTTON_PADDING, ScreenDimen.height
                        - GameValues.BUTTON_PADDING, paintText);
            } else
                canvas.drawText("Music Off", soundBtn.xPos
                        - GameValues.BUTTON_PADDING, ScreenDimen.height
                        - GameValues.BUTTON_PADDING, paintText);

            // COINS:
            txt = Utility.formatNumber(Utility.getCoins(context));
            canvas.drawText(txt, storeBtn.xPos - GameValues.BUTTON_PADDING,
                    storeBtn.yPos + GameValues.BUTTON_SCALE, paintText);
            paintText.getTextBounds(txt, 0, txt.length(), result);
            canvas.drawBitmap(
                    BitmapLoader.coin,
                    (storeBtn.xPos - result.width())
                            - (GameValues.COIN_SCALE + GameValues.BUTTON_PADDING * 1.225f),
                    (storeBtn.yPos + GameValues.BUTTON_SCALE)
                            - GameValues.COIN_SCALE, paint);

            // SCORE & STATS:
            txt = ("Played: " + player.gamesPlayed);
            paintText.setColor(0xffe5e4a0);
            paintText.setTextAlign(Align.LEFT);
            paintText.setTextSize(ScreenDimen.width / 19.25f);
            paintText.getTextBounds(txt, 0, txt.length(), result);
            canvas.drawText(
                    txt,
                    (achievBtn.xPos + GameValues.BUTTON_SCALE + GameValues.BUTTON_PADDING),
                    (achievBtn.yPos + GameValues.BUTTON_SCALE), paintText);
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

            canvas.drawText(
                    scoreText,
                    (achievBtn.xPos + GameValues.BUTTON_SCALE + GameValues.BUTTON_PADDING),
                    (achievBtn.yPos + GameValues.BUTTON_SCALE)
                            - result.height(), paintText);

            // MODE:
            paintText.setTextAlign(Align.CENTER);
            paintText.setTextSize(ScreenDimen.width / 27);
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
            canvas.drawText(txt, modeBtn.xPos + (GameValues.BUTTON_SCALE / 2),
                    (modeBtn.yPos + GameValues.BUTTON_SCALE)
                            + (GameValues.BUTTON_PADDING * 1.15f), paintText);
            // RANK:

        } else if (state == GameState.Playing) {
            // SCORE
            paintText.setColor(0xffe5e4a0);
            paintText.setTextSize((ScreenDimen.width / 4.1f) * scoreTextMult);
            paintText.setTextAlign(Align.CENTER);
            paintText.getTextBounds(String.valueOf(player.score), 0, String
                    .valueOf(player.score).length(), result);
            paintText.setAlpha(255);
            if (player.score > 0) {
                canvas.drawText(String.valueOf(player.score),
                        ScreenDimen.getCenterX(),
                        scoreDisplay.yPos + (result.height() / 2), paintText);
            } else {
                canvas.drawText(String.valueOf(1), ScreenDimen.getCenterX(),
                        scoreDisplay.yPos + (result.height() / 2), paintText);
            }
            paintText.getTextBounds("0", 0, "0".length(), result);
            paintText.setTextSize(ScreenDimen.width / 15.5f);
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
            paintText.setColor(0xffe5e4a0);
            paintText.setAlpha(255);
            canvas.drawText(txt, ScreenDimen.getCenterX(), scoreDisplay.yPos
                    + (result.height()), paintText);
        }

        // INTRO
        if (introShowing) {
            paint.setColor(0xff3e3e3e);
            canvas.drawRect(0, 0, ScreenDimen.width, ScreenDimen.height, paint);
            paintText.setColor(0xffe5e4a0);
            paintText.setTextSize(ScreenDimen.width / 9);
            paintText.setTextAlign(Align.CENTER);
            canvas.drawText("The Big Shots", ScreenDimen.getCenterX(),
                    ScreenDimen.getCenterY(), paintText);
            paintText.setTextSize(ScreenDimen.width / 25);
            paintText.setTextAlign(Align.CENTER);
            canvas.drawText("Thank you for Playing!", ScreenDimen.getCenterX(),
                    ScreenDimen.height - GameValues.BUTTON_PADDING, paintText);
            paint.setColor(0xffe532cd);
            canvas.drawRect(ScreenDimen.getCenterX()
                            - (GameValues.LOADING_BAR_WIDTH / 2), ScreenDimen.height
                            - GameValues.LOADING_BAR_WIDTH / 2f,
                    (ScreenDimen.getCenterX() - (GameValues.LOADING_BAR_WIDTH / 2))
                            + loadWidth,
                    (ScreenDimen.height - GameValues.LOADING_BAR_WIDTH / 2f)
                            + GameValues.LOADING_BAR_WIDTH / 10, paint);
        }
    }

    @Override
    public void getSprites() {

    }

    @Override
    public void disposeSprites() {
        bitmapLoader.dispose();
        Utility.disposeFont();
    }

    @Override
    public void drawHUD() {

    }
}
