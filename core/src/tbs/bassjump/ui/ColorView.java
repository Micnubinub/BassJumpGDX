package tbs.bassjump.ui;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by root on 3/01/15.
 */
public class ColorView extends View {
    private static Color c = new Color();
    private int r, cx, cy, color;

    public ColorView(int color) {
        this.color = color;
    }


    @Override
    protected void onDraw(ShapeRenderer canvas) {

        c.set(0xffffffff);
        canvas.setColor(c);
        canvas.circle(cx, cy, r);

        c.set(color);
        canvas.circle(cx, cy, Math.round(r * 0.95f));
    }

    public void setSize(int w, int h) {
        cx = w / 2;
        cy = h / 2;
        r = Math.min(cx, cy);
    }
}
