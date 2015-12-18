package tbs.bassjump.view_lib;

import tbs.bassjump.Game;

/**
 * Created by linde on 09-Dec-15.
 */
public class ViewPager extends ViewGroup {
    private static int coins;
    private TextView title, shapesTitle, colorsTitle, numCoins;
    private ListView shapes, colors;

    //Todo ensure all pages are the same size as the parent
    //Todo make adapter for both the pages and the titles* optional
    private Background selectedItemBackground;
    public View.OnClickListener titleClickListener = new OnClickListener() {
        @Override
        public void onClick(View view, int x, int y) {
            if (view instanceof TextView) {
                if (shapesTitle.getText().equals(((TextView) view).getText())) {
                    shapes.setVisible(true);
                    shapes.setBackground(selectedItemBackground);
                    colors.setVisible(false);
                    colors.setBackground(null);
                } else {
                    shapes.setVisible(false);
                    shapes.setBackground(null);
                    colors.setVisible(true);
                    colors.setBackground(selectedItemBackground);
                }
            }
        }
    };

    //Todo views are the title, pager tab strip and pages

    public ViewPager() {
        title = new TextView(23);
        shapesTitle = new TextView(23);
        colorsTitle = new TextView(23);
        numCoins = new TextView(23);

        shapes = new ListView();
        colors = new ListView();

        title.setText("Shop");
        shapesTitle.setText("Shapes");
        numCoins.setText("0");
        colorsTitle.setText("Colors");

        shapesTitle.setOnClickListener(titleClickListener);
        colorsTitle.setOnClickListener(titleClickListener);

        shapes.setAdapter(new ShapeAdapter());
        colors.setAdapter(new ColorAdapter());

        shapes.setVisible(true);
        colors.setVisible(false);

        setBackground(new Background(0x444444ff, Background.Type.COLOR));
        selectedItemBackground = new Background(0x888888ff, Background.Type.COLOR);
    }

    public static void setNumCoins(int coins) {
        coins = coins < 0 ? 0 : coins;
        ViewPager.coins = coins;
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean click(UniversalClickListener.TouchType touchType, int xPos, int yPos) {
        rect.set(lastRelX + x, lastRelY + y, w, h);
        if (!rect.contains(xPos, yPos)) {
            visible = false;
            return true;
        }
        return super.click(touchType, xPos, yPos);
    }

    @Override
    public void draw(float relX, float relY, float parentRight, float parentTop) {
        //Todo draw title and tab strip
        if (!visible)
            return;

        Game.beginRenderer();

        color.set(0x00000099);
        color.a = 0.2f;
        Game.renderer.setColor(color);
//        Game.renderer.rect(0, 0, Game.w, Game.h);

        drawBackground(relX, relY);
        setDimens();

        final float drawR = Math.min(parentRight, relX + x + w);
        final float drawT = Math.min(parentTop, relY + y + h);

        title.draw(relX + x, relY + y, drawR, drawT);
        colorsTitle.draw(relX + x, relY + y, drawR, drawT);
        shapesTitle.draw(relX + x, relY + y, drawR, drawT);

        shapes.setHeight(h - title.h - colorsTitle.h);
        colors.setHeight(h - title.h - colorsTitle.h);

        if (colors.visible)
            colors.draw(relX + x, relY + y, drawR, drawT);

        if (shapes.visible)
            shapes.draw(relX + x, relY + y, drawR, drawT);
    }

    private void setDimens() {
        title.setWidth(w);
        colorsTitle.setWidth(w / 2);
        shapesTitle.setWidth(w / 2);
        shapesTitle.setX((int) w / 2);

        title.y = h - title.h;

        final float pageH = h - title.h - Math.min(colorsTitle.h, shapesTitle.h);

        shapesTitle.y = pageH;
        colorsTitle.y = pageH;

        shapes.setWidth(w);
        shapes.setHeight(h);

        colors.setWidth(w);
        colors.setHeight(h);

    }

    public static class ShapeAdapter extends Adapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position) {
            return null;
        }
    }

    public static class ColorAdapter extends Adapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position) {
            return null;
        }
    }
}
