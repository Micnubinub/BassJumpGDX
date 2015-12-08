package tbs.bassjump.managers;


import com.badlogic.gdx.graphics.Texture;


public class BitmapLoader {

    // PLAYER:
    public static Texture player;

    // LOADING
    public static Texture leader;
    public static Texture sound;
    public static Texture soundO;
    public static Texture achiv;
    public static Texture store;
    public static Texture achievm;
    public static Texture share;

    // MODES:
    public static Texture modeArcade;
    public static Texture modeRecruit;
    public static Texture modeUltra;
    public static Texture modeSingular;
    public static Texture modeSpeed;

    // COIN:
    public static Texture coin;

    public BitmapLoader() {

        // PLAYER:
        player = new Texture("player");

        leader = new Texture("leader.png");
        sound = new Texture("sound.png");

        soundO = new Texture("soundoff.png");

        achiv = new Texture("achiv.png");

        store = new Texture("store.png");

        achievm = new Texture("achiv2.png");
        share = new Texture("share.png");


        // MODE:
        modeArcade = new Texture("modearcade.png");

        modeRecruit = new Texture("moderecruit.png");

        modeUltra = new Texture("modeultra.png");

        modeSingular = new Texture("modesingul.png");

        modeSpeed = new Texture("modespeed.png");


        // COIN:
        coin = new Texture("coin.png");
    }

    public void dispose(){
         player.dispose();

        // LOADING
        leader.dispose();
        sound.dispose();
        soundO.dispose();
        achiv.dispose();
        store.dispose();
        achievm.dispose();
        share.dispose();

        // MODES:
        modeArcade.dispose();
        modeRecruit.dispose();
        modeUltra.dispose();
        modeSingular.dispose();
        modeSpeed.dispose();

        // COIN:
        coin.dispose();
    }

}
