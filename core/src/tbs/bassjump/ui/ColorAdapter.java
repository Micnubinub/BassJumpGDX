package tbs.bassjump.ui;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.Arrays;

import tbs.bassjump.Game;
import tbs.bassjump.GameValues;
import tbs.bassjump.Utility;

/**
 * Created by mike on 3/7/16
 */

public class ColorAdapter extends Adapter {
    private static ColorView colorView = new ColorView();
    private static ShaderProgram[] shaderPrograms;
    private static boolean[] itemsBought;
    private static int equippedItem;
    private static BuyButton[] buyButtons;

    public ColorAdapter() {
        if (shaderPrograms != null)
            dispose();
        refreshRegions();
    }

    public static void refreshRegions() {
        dispose();
        shaderPrograms = new ShaderProgram[Utility.dark.length];
        for (int i = 0; i < Utility.dark.length; i++) {
            shaderPrograms[i] = Utility.getCircleViewShaderProgram(Utility.light[i], Utility.dark[i]);
        }
    }

    public static void getItemBought() {
        equippedItem = Utility.getEquippedColor();

        if (itemsBought == null)
            itemsBought = new boolean[Utility.colorNames.length];

        final String[] boughtColors = Utility.getBoughtColors();
        buyButtons = new BuyButton[Utility.colorNames.length];
        for (int i = 0; i < Utility.colorNames.length; i++) {
            itemsBought[i] = Utility.contains(boughtColors, String.valueOf(i));
            final BuyButton buyButton = new BuyButton(i);
            buyButton.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.7f));
            buyButtons[i] = buyButton;
        }
        Utility.print("colors > " + Arrays.toString(itemsBought));
    }

    public static void dispose() {
        if (shaderPrograms == null)
            return;

        for (ShaderProgram shaderProgram : shaderPrograms) {
            Utility.dispose(shaderProgram);
        }
        shaderPrograms = null;
    }

    @Override
    public int getCount() {
        return Utility.dark.length;
    }

    @Override
    public boolean click(int x, int y) {
        for (final BuyButton buyButton : buyButtons) {
            if (buyButton.click(x, y)) {
                final int position = buyButton.position;
                if (itemsBought[position]) {
                    Utility.equipColor(position);
                    Game.shaderProgram = Utility.getCarShaderProgram(Utility.light[position], Utility.dark[position]);
                    equippedItem = position;
                } else {
                    if (Game.numCoins < Utility.COLOR_PRICE) {

                    } else {
                        Utility.addBoughtColors(position);
                        itemsBought[position] = true;
                        Game.numCoins -= Utility.COLOR_PRICE;
                        Utility.saveCoins(Game.numCoins);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public View getView(int position) {
        ColorView.text.setText(Utility.colorNames[position]);
        ColorView.price.setText(Utility.COLOR_PRICE_S);
        ColorView.shaderProgram = shaderPrograms[position];

        final BuyButton buyButton = buyButtons[position];
        try {
            if (itemsBought[position]) {
                if (equippedItem == position) {
                    buyButton.setText("EQUIPPED");
                    buyButton.setButtonMode(BuyButton.EQUIPPED);
                } else {
                    buyButton.setText("EQUIP");
                    buyButton.setButtonMode(BuyButton.EQUIP);
                }
            } else {
                buyButton.setText("BUY");
                buyButton.setButtonMode((Game.numCoins < Utility.COLOR_PRICE) ? BuyButton.CANNOT_BUY : BuyButton.BUY);
            }
            ColorView.buyButton = buyButton;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colorView;
    }

    private static class ColorView extends View {
        public static final TextView text = new TextView(TextView.Gravity.LEFT);
        public static final TextView price = new TextView();
        public static BuyButton buyButton;
        public static ShaderProgram shaderProgram;
        private static int pad, scale;

        public ColorView() {
            ColorView.text.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.7f));
            text.setTextColor(0xffffffff);
            h = GameValues.COLOR_CAR_SCALE * 1.16f;
            scale = GameValues.COLOR_CAR_SCALE;
            pad = (int) (GameValues.COLOR_CAR_SCALE * 0.08f);
            text.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.7f));
            price.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.7f));
        }

        @Override
        public void draw(float relX, float relY, float parentRight, float parentTop) {
            final float textY = relY + (h / 2);
            text.draw(relX + pad + pad + scale, textY, parentRight, parentTop);

            final float center = relY + (h / 2);
            final float priceY = center + (pad / 2);
            final float priceX = w - price.w;

            price.draw(priceX, priceY, parentRight, parentTop);
            Game.spriteBatch.draw(BitmapLoader.diamond, priceX - (GameValues.TITLE_HEIGHT * 0.7f), priceY + (GameValues.TITLE_HEIGHT * 0.26f), GameValues.TITLE_HEIGHT * 0.52f, GameValues.TITLE_HEIGHT * 0.46f);
            buyButton.draw(w - buyButton.w, priceY - (pad) - (buyButton.h / 2) - GameValues.CORNER_SCALE, parentRight, parentTop);

            Game.spriteBatch.setShader(shaderProgram);
            Utility.drawTextureWithRotation(BitmapLoader.storeItemTestMix, pad + relX + x, pad + relY + y, scale, scale, 45);
            Game.spriteBatch.setShader(null);
        }

        @Override
        public void dispose() {
        }

        @Override
        public boolean fling(float vx, float vy) {
            return false;
        }

        @Override
        public String toString() {
            return text.text;
        }

    }
}
