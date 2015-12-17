package tbs.bassjump.view_lib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import tbs.bassjump.GameController;

/**
 * Created by Michael on 4/18/2015.
 */

public class UniversalClickListener implements InputProcessor, GestureDetector.GestureListener {
    public static boolean isTouchDownSinceLastPan;
    public static boolean shouldFlingHorizontally, shouldFlingVertically;
    protected static float flingX, flingY;
    static float initZoom;
    static float tDownX, tDownY;
    private static int w, h;
    private static UniversalClickListener universalClickListener;

    private UniversalClickListener() {
    }

    public static UniversalClickListener getUniversalClickListener() {
        if (universalClickListener == null) {
            universalClickListener = new UniversalClickListener();
            Gdx.input.setInputProcessor(new InputMultiplexer(universalClickListener, new GestureDetector(universalClickListener), new GameController()));
            w = Gdx.graphics.getWidth();
            h = Gdx.graphics.getHeight();
        }
        if (Gdx.input.getInputProcessor() == null)
            Gdx.input.setInputProcessor(new InputMultiplexer(universalClickListener, new GestureDetector(universalClickListener), new GameController()));

        return universalClickListener;
    }

    public static void click(TouchType touchType, int x, int y) {
        y = Gdx.graphics.getHeight() - y;
        HUDManager.getHUDManager().click(touchType, x, y);
    }

    public static void confirmClick() {

    }

    public static void confirmFling() {
        isTouchDownSinceLastPan = false;
    }

    public static float getInitialTouchDownX() {
        return tDownX;
    }

    public static float getInitialTouchDownY() {
        return tDownY;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        initZoom = initZoom < 1 ? 1 : initZoom;
        click(TouchType.TOUCH_DOWN, (int) x, (int) y);
        isTouchDownSinceLastPan = true;
//Todo        initZoom = camera.getZoom();
        tDownX = x;
        tDownY = h - y;
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        isTouchDownSinceLastPan = true;
        click(TouchType.CLICK, (int) x, (int) y);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        //TODO implement longPres
        // s
        return HUDManager.getHUDManager().longClick(TouchType.LONG_CLICK, (int) x, (int) y);
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        isTouchDownSinceLastPan = false;

//        final double vector = Math.sqrt((velocityX * velocityX) + (velocityY * velocityY));
//        final double vectorScreen = Math.sqrt((width * width) + (height * height));
//        isTouchDownSinceLastPan = false;
//        panAnimator.setDuration((vector / vectorScreen) * 250);
//        panAnimator.start();
//        flingX = velocityX / 6;
//        flingY = velocityY / 6;
//        initCamXBeforeFling = camera.position.x;
//        initCamYBeforeFling = camera.position.y;
        return HUDManager.getHUDManager().fling(velocityX, velocityY);
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        shouldFlingHorizontally = deltaX != 0;
        shouldFlingVertically = deltaY != 0;
        return HUDManager.getHUDManager().drag(tDownX, tDownY, deltaX, deltaY);
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        //TODO fix pinch issues

//        camera.setZoom(initZoom * initialPointer1.dst(initialPointer2) / pointer1.dst(pointer2));
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
//        switch (keycode) {
//            case Input.Keys.LEFT:
//                UILib.x -= 15;
//                break;
//            case Input.Keys.RIGHT:
//                UILib.x += 15;
//                break;
//
//            case Input.Keys.UP:
//                UILib.y += 15;
//                break;
//
//            case Input.Keys.DOWN:
//                UILib.y -= 15;
//                break;
//        }
        return false;
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tDownX = screenX;
        tDownY = h - screenY;
        isTouchDownSinceLastPan = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        click(TouchType.TOUCH_UP, screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //Todo handle touch events here, have a boolean called touch intercepted
//        return HUDManager.getHUDManager().drag(tDownX, tDownY, screenX - tDownX, screenY - tDownY);
        return false;
    }

    public void print(String s) {
        Utility.print(s);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
//        float zoom = camera.getZoom();
//
//        if (amount > 0)
//            zoom += 0.02f;
//        else if (amount < 0)
//            zoom -= 0.02f;
//
//        camera.setZoom(zoom);
        return false;
    }

    public enum TouchType {
        CLICK, LONG_CLICK, TOUCH_DOWN, TOUCH_UP, FLING, PAN
    }

    public interface OnClickListener {
        void onClick(View view);
    }

    public interface OnTouchListener {
        void onTouch(final View view, final TouchType touchType);

        void onDrag(final int screenX, final int screenY);

        void onFling(final float velocityX, final float velocityY);
    }

}
