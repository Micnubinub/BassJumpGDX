package tbs.bassjump.reference;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class GameController implements InputProcessor {

    public static void pressScreen(int x, int y) {
        y = GameContainer.h - y;
        if (GameContainer.state == GameState.Menu) {
            if (GameContainer.rateButton.isClicked(x, y)) {
//                // GameContainer.log("menu > rate");
          /*Todo    vGdx.net.openURI(  final String appPackageName = Game.context.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    Game.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    Game.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }*/
            } else if (GameContainer.leaderButton.isClicked(x, y)) {
//                // GameContainer.log("menu > lead");
/*          Todo      if (BaseGameActivity.getApiClient().isConnected()) {
                    ((BaseGameActivity) Game.context).startActivityForResult(
                            Games.Leaderboards.getLeaderboardIntent(
                                    MainActivity.getApiClient(), GameValues.leaderboardID),
                            10101);
                } else {
                    // LOGIN TO PLAY SERVICES:
                    ((BaseGameActivity) Game.context).getGameHelper()
                            .beginUserInitiatedSignIn();
                }*/
            } else if (GameContainer.storeButton.isClicked(x, y)) {
//                // GameContainer.log("menu > store");
                GameContainer.state = GameState.Store;
                GameContainer.showStore();
            } else if (GameContainer.gambleButton.isClicked(x, y)) {
//                // GameContainer.log("menu > gamble");
                // START GAMBLING:
                if (GameContainer.player.coins >= GameContainer.casino.playCost) {
                    GameContainer.casino.moneySpent = GameContainer.casino.playCost;
                    GameContainer.player.coins -= GameContainer.casino.playCost;
                    GameContainer.casino.generateRewards();
                    GameContainer.player.earnCoinAnim(x, y, 0);
                    GameContainer.state = GameState.Casino;
                    GameContainer.moneySound.play();
                } else {
                    GameContainer.buttonSound.play();
                }
            } else {
                // GameContainer.log("menu > play");
                GameContainer.buttonSound.play();
                GameContainer.state = GameState.Playing;
            }
        } else if (GameContainer.state == GameState.Playing) {
            // GameContainer.log("play > jump");
            GameContainer.player.jump();
        } else if (GameContainer.state == GameState.Death) {
            // GameContainer.log("death > rate");
            if (GameContainer.homeButton.isClicked(x, y)) {
                // GameContainer.log("death > setup");
                GameContainer.setup();
            } else if (GameContainer.shareButton.isClicked(x, y)) {
                // GameContainer.log("death > share");
//                MainActivity.unlockAchievement("CgkIxIfix40fEAIQDA");
//      Todo          Game.Share(Game.takeScreenShot());
            } else if (GameContainer.retryButton.isClicked(x, y)) {
                // GameContainer.log("death > retry");
                GameContainer.setup();
                GameContainer.state = GameState.Playing;
            } else if (GameContainer.adButton.isClicked(x, y)) {
                // GameContainer.log("death > ad");
                // SHOW AD:
                GameContainer.showAd(true);
                GameContainer.adButton.active = false;
            } else if (GameContainer.likeButton.isClicked(x, y)) {
                // GameContainer.log("death > like");
                Gdx.net.openURI("https://www.facebook.com/AndroidHackerApp");
                GameContainer.likeButton.active = false;
            } else if (GameContainer.buyButton.isClicked(x, y)) {
                // GameContainer.log("death > buy");
                // EARN MONEY REWARD:
//        Todo        MainActivity.unlockAchievement("CgkIxIfix40fEAIQCA");
                GameContainer.moneySound.play();
                GameContainer.player.earnCoinAnim(x, y, Utility.getRandom(1, 20));
                GameContainer.buyButton.active = false;
                if (GameContainer.player.score > 0)
                    GameContainer.reviveButton.active = GameContainer.player.coins >= GameContainer.revivalCost;
            } else if (GameContainer.reviveButton.isClicked(x, y)) {
                // GameContainer.log("death > revive");
                // REVIVAL LOGIC:
                GameContainer.reviveButton.active = false;
                GameContainer.player.coins -= GameContainer.revivalCost;
                GameContainer.player.saveData();
                GameContainer.player.revive();
                GameContainer.state = GameState.Playing;
            }
        } else if (GameContainer.state == GameState.Casino) {
            if (!GameContainer.casino.rewardAnim) {
                // GameContainer.log("casino > reward anim");
                for (int i = 0; i < GameContainer.casino.items.size(); ++i) {
                    if (GameContainer.casino.items.get(i).isClicked(x, y)) {
                        for (int z = 0; z < GameContainer.casino.items.size(); ++z) {
                            GameContainer.casino.items.get(z).active = false;
                        }
                        // GET REWARD:
                        GameContainer.casino.playerSelectButton(i, x, y);
                        GameContainer.player.earnCoinAnim(x, y, 0);
                    }
                }
            } else {

                if (GameContainer.homeButton2.isClicked(x, y)) {
                    // GameContainer.log("casino > home");
                    GameContainer.state = GameState.Menu;
                    GameContainer.player.saveData();
                }
            }
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
