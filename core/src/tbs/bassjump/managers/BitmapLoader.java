package tbs.bassjump.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import tbs.bassjump.objects.Player;


public class BitmapLoader {
    // LOADING
    public static TextureRegion leader;
    public static TextureRegion sound;
    public static TextureRegion soundO;
    public static TextureRegion achiv;
    public static TextureRegion store;
    public static TextureRegion achievm;
    public static TextureRegion share;
    // MODES:
    public static TextureRegion modeArcade;
    public static TextureRegion modeRecruit;
    public static TextureRegion modeUltra;
    public static TextureRegion modeSingular;
    public static TextureRegion modeSpeed;
    // COIN:
    public static TextureRegion coin;
    public static TextureAtlas sprites;
    //Todo make texture atlas
    private static TextureAtlas atlas;

    public BitmapLoader() {
        init();
    }

    public void init() {

        try {
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }

        atlas = new TextureAtlas(Gdx.files.internal("textures"));
        sprites = new TextureAtlas(Gdx.files.internal("sprites"));

        // PLAYER:
        leader = atlas.findRegion("leader");
        sound = atlas.findRegion("sound");
        soundO = atlas.findRegion("soundoff");
        achiv = atlas.findRegion("achiv");
        store = atlas.findRegion("store");
        achievm = atlas.findRegion("achiv2");
        share = atlas.findRegion("share");

        // MODE:
        modeArcade = atlas.findRegion("modearcade");
        modeRecruit = atlas.findRegion("moderecruit");
        modeUltra = atlas.findRegion("modeultra");
        modeSingular = atlas.findRegion("modesingul");
        modeSpeed = atlas.findRegion("modespeed");

        // COIN:
        coin = atlas.findRegion("coin");
        Player.reloadPlayerTexture();
    }

    public void dispose() {
        if (atlas != null)
            atlas.dispose();

        if (sprites != null)
            sprites.dispose();
    }

}
