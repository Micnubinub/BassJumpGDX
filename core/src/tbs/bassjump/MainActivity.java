package tbs.bassjump;


import tbs.bassjump.fragments.GetCoinsFragment;
import tbs.bassjump.utility.AdManager;

public class MainActivity extends BaseGameActivity {

    // TAG & ACTIVITY:
    public static final String TAG = "Mini_RPG";
//    public static View view;

    // SAVE DATA:
    public static AdManager adManager;
    public static MainActivity mainActivity;
    public static GetCoinsFragment getCoinsFragment = new GetCoinsFragment();

    // Other apps ad
    public static int adsWatched;

    // PURCHASES:


    // ADS:
    public static boolean showAds;

    //tbs.bassjump.reference.Game container

    public static void unlockAchievement(String id) {
//        if (getApiClient().isConnected()) {
//            Games.Achievements.unlock(getApiClient(), id);
//        }
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    private static void log(String step) {
        Utility.print("msg > "+ step);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adManager = new AdManager(this);

        mainActivity = this;
        // ACHIEVEMENT:
        unlockAchievement("CgkIvYbi1pMMEAIQDA");

        // SETUP:
        Utility.setupRandom();
        ScreenDimen.setup();
        Game.init();
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
                Game.pauseSong();
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

    }






}