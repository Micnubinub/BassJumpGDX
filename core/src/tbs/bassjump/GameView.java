package tbs.bassjump;


import java.nio.IntBuffer;


public class GameView extends View {
    public static int background;
    public static long lastUpdate = System.currentTimeMillis();
    public static ShareButtonClicked shareButtonClicked;
    static int delta, sw, sh;

    public GameView( ) {

//        background = tbs.bassjump.Utility.getColor(tbs.bassjump.Utility.getEquippedBackground());
        background = 0xff222222;

    }

    public static void drawGame() {
        delta = (int) (System.currentTimeMillis() - lastUpdate);
        GameValues.SPEED_FACTOR = (int) ((GameValues.SPEED_FACTOR_ORIGINAL * GameValues.SPEED_BONUS) * delta);
        if (GameValues.SPEED_FACTOR < 1)
            GameValues.SPEED_FACTOR = 1;
        GameValues.PLAYER_JUMP_SPEED = (GameValues.SPEED_FACTOR * GameValues.PLAYER_JUMP_SPEED_MULT);
        lastUpdate = System.currentTimeMillis();
        Game.update();
//  todo clear color      Game.paint.setColor(background);

    }

    public void onTouch() {
        switch (e.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                int x = (int) e.getX();
                int y = (int) e.getY();
                if (x >= Game.leaderBtn.xPos
                        && x <= Game.leaderBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.leaderBtn.yPos
                        && y <= Game.leaderBtn.yPos + GameValues.BUTTON_SCALE) {
                    // LEADER:
                    Log.e("learderBoardClick > ", "registered");
                    if (BaseGameActivity.getApiClient().isConnected()) {
                        String leadID = "";
                        if (Game.mode == GameMode.Arcade) {
                            leadID = "CgkIvYbi1pMMEAIQBg";
                        } else if (Game.mode == GameMode.Recruit) {
                            leadID = "CgkIvYbi1pMMEAIQBw";
                        } else if (Game.mode == GameMode.Ultra) {
                            leadID = "CgkIvYbi1pMMEAIQEQ";
                        } else if (Game.mode == GameMode.SpeedRunner) {
                            leadID = "CgkIvYbi1pMMEAIQFA";
                        } else { // Singular
                            leadID = "CgkIvYbi1pMMEAIQEg";
                        }
                        Log.e("learderBoardStaring > ", new String(leadID));
                        ((FragmentActivity) )
                                .startActivityForResult(
                                        Games.Leaderboards
                                                .getLeaderboardIntent(
                                                        MainActivity.getApiClient(),
                                                        leadID),
                                        10101);
                    } else {
                        Log.e("learderBoardStaring > ", "not connected");
                        ((BaseGameActivity) ).getGameHelper()
                                .beginUserInitiatedSignIn();
                    }
                } else if (x >= Game.achievBtn.xPos
                        && x <= Game.achievBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.achievBtn.yPos
                        && y <= Game.achievBtn.yPos + GameValues.BUTTON_SCALE) {
                    if (BaseGameActivity.getApiClient().isConnected()) {
                        try {
                            ((FragmentActivity) ).startActivityForResult(
                                    Games.Achievements
                                            .getAchievementsIntent(MainActivity
                                                    .getApiClient()), 10101);
                        } catch (Exception ex) { // BAD PRATICE :P
                            System.out.println(e);
                        }
                    } else {
                        ((BaseGameActivity) ).getGameHelper()
                                .beginUserInitiatedSignIn();
                    }
                } else
                    GameController.pressed((int) (e.getX()), (int) (e.getY()), p);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                GameController.pressed((int) (e.getX(p - 1)),
                        (int) (e.getY(p - 1)), p);
                break;

            case MotionEvent.ACTION_UP:
                GameController.released((int) (e.getX()), (int) (e.getY()), p);
                break;
        }
    }

}