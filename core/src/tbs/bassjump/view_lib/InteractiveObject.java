package tbs.bassjump.view_lib;


/**
 * Created by Michael on 2/1/2015.
 */
public interface InteractiveObject {
    Rect rect = new Rect();
    Rect rect2 = new Rect();

    boolean click(UniversalClickListener.TouchType touchType, int xPos, int yPos);

    boolean longClick(UniversalClickListener.TouchType touchType, int xPos, int yPos);

    boolean drag(float startX, float startY, float dx, float dy);

    boolean fling(float vx, float vy);
}
