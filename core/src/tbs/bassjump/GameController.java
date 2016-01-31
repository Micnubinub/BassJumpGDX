package tbs.bassjump;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import tbs.bassjump.managers.StoreManager;

public class GameController implements InputProcessor {


    public static void pressed(int x, int y, int index) {

    }

    public static void released(int x, int y, int index) {

    }

    public static void pressScreen(int x, int y) {
        if (!Game.introShowing) {
            if (Game.state == GameState.Menu) {
                if (x >= Game.soundBtn.xPos
                        && x <= Game.soundBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.soundBtn.yPos
                        && y <= Game.soundBtn.yPos + GameValues.BUTTON_SCALE) {
                    // SOUND:
                    if (Game.isMusicEnabled) {
                        // TURN OFF
                        Game.isMusicEnabled = false;
                        try {
                            Game.pauseMusic();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Utility.saveString("musicOn", "off");
                    } else {
                        // TURN ON
                        Game.isMusicEnabled = true;
                        try {
                            Game.playMusic();
                        } catch (Exception r) {
                            r.printStackTrace();
                        }
                        Utility.saveString("musicOn", "on");
                    }
                } else if (x >= Game.rateBtn.xPos
                        && x <= Game.rateBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.rateBtn.yPos
                        && y <= Game.rateBtn.yPos + GameValues.BUTTON_SCALE) {
                    // FB PAGE:
                    Utility.openLink("https://www.facebook.com/AndroidHackerApp");

                } else if (x >= Game.modeBtn.xPos
                        && x <= Game.modeBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.modeBtn.yPos
                        && y <= Game.modeBtn.yPos + GameValues.BUTTON_SCALE) {
                    // STORE:
                    if (Game.mode == GameMode.Recruit) {
                        Game.mode = GameMode.Arcade;
                        Utility.saveString("gMode", "arcade");
                    } else if (Game.mode == GameMode.Arcade) {
                        Game.mode = GameMode.Ultra;
                        Utility.saveString("gMode", "ultra");
                    } else if (Game.mode == GameMode.Ultra) {
                        Game.mode = GameMode.Singularity;
                        Utility.saveString("gMode", "singul");
                    } else if (Game.mode == GameMode.Singularity) {
                        Game.mode = GameMode.SpeedRunner;
                        GameValues.PLAYER_JUMP_SPEED_MULT = 8;
                        Utility.saveString("gMode", "speed");
                    } else if (Game.mode == GameMode.SpeedRunner) {
                        Game.mode = GameMode.Recruit;
                        Utility.saveString("gMode", "recruit");
                        GameValues.PLAYER_JUMP_SPEED_MULT = 3;
                    }
                } else if (x >= Game.shareBtn.xPos
                        && x <= Game.shareBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.shareBtn.yPos
                        && y <= Game.shareBtn.yPos + GameValues.BUTTON_SCALE) {
                    // SHARE:
                    Utility.openLink("http://thebigshots.net");
                    // tbs.bassjump.Utility.showToast("Share Coming Soon!", tbs.bassjump.reference.);
                } else if (x >= Game.storeBtn.xPos
                        && x <= Game.storeBtn.xPos + GameValues.BUTTON_SCALE
                        && y >= Game.storeBtn.yPos
                        && y <= Game.storeBtn.yPos + GameValues.BUTTON_SCALE) {
                    // STORE:
                    final StoreManager manager = new StoreManager();
                    manager.showStore();
                } else {
                    // PLAY:
                    Game.state = GameState.Playing;
                    Game.player.jump();
                    Game.showAnimatedText("GO!", (Game.w / 2),
                            (Game.h / 2) + ((Game.h) / 6),
                            (Game.h) / 150, 9, 255, 0);
                }
            } else if (Game.state == GameState.Playing) {
                Game.player.jump();
            }
        } else {
            //tbs.bassjump.reference.Game.introShowing = false; // SKIP
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        pressScreen(x, y);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                pressScreen(0, 0);
                break;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
