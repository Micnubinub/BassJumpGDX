package tbs.bassjump.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;

import tbs.bassjump.Game;
import tbs.bassjump.GameValues;
import tbs.bassjump.Utility;
import tbs.bassjump.managers.BitmapLoader;


/**
 * Created by mike on 3/7/16.
 */
public class CarsAdapter extends Adapter {
    public static BuyButton[] buyButtons;
    private static CarView carView = new CarView();
    private static TextureRegion[] regions = new TextureRegion[Utility.cars.length];
    private static boolean[] itemsBought;
    private static int equippedItem;

    public static void getItemBought() {
        equippedItem = Utility.getEquippedCar();

        if (itemsBought == null)
            itemsBought = new boolean[Utility.colorNames.length];

        final String[] boughtCars = Utility.getBoughtCars();
        buyButtons = new BuyButton[Utility.colorNames.length];
        for (int i = 0; i < Utility.colorNames.length; i++) {
            itemsBought[i] = Utility.contains(boughtCars, String.valueOf(i));
            final BuyButton buyButton = new BuyButton(i);
            buyButton.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.7f));
            buyButtons[i] = buyButton;
        }
        Utility.print("cars > " + Arrays.toString(itemsBought));
    }

    public static void refreshRegions() {
        for (int i = 0; i < Utility.cars.length; i++) {
            regions[i] = BitmapLoader.atlas.findRegion(Utility.cars[i]);
        }
    }

    @Override
    public int getCount() {
        return Utility.cars.length;
    }

    @Override
    public boolean click(int x, int y) {
        for (final BuyButton buyButton : buyButtons) {
            if (buyButton.click(x, y)) {
                final int position = buyButton.position;
                if (itemsBought[position]) {
                    Utility.equipCar(position);
                    equippedItem = position;
                } else {
                    if (Game.numCoins < Utility.carPrices[position]) {

                    } else {
                        Utility.addBoughtCar(position);
                        itemsBought[position] = true;
                        Game.numCoins -= Utility.carPrices[position];
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
        CarView.text.setText(Utility.carNames[position]);
        CarView.price.setText(Utility.carPricesS[position]);
        CarView.region = regions[position];
        if (CarView.region == null) {
            refreshRegions();
            CarView.region = regions[position];
        }
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
            CarView.buyButton = buyButton;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carView;
    }

    public void dispose() {

    }

    private static class CarView extends View {
        public static final TextView price = new TextView();
        public static final TextView text = new TextView(TextView.Gravity.LEFT);
        public static BuyButton buyButton;
        public static TextureRegion region;
        private static int pad, scale;

        public CarView() {
            text.setTextColor(0xffffffff);
            pad = (int) (GameValues.COLOR_CAR_SCALE * 0.08f);
            h = GameValues.CAR_HEIGHT + pad + pad;
            scale = GameValues.COLOR_CAR_SCALE;

            text.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.7f));
            price.setTextScale(Utility.getScale(GameValues.TITLE_HEIGHT * 0.7f));
        }

        @Override
        public void draw(float relX, float relY, float parentRight, float parentTop) {
            final float textY = relY + (h / 2);
            text.draw(relX + pad + pad + GameValues.CAR_WIDTH, textY, parentRight, parentTop);

            final float center = relY + (h / 2);
            final float priceY = center + (pad / 2);
            final float priceX = w - price.w;
            price.draw(priceX, priceY, parentRight, parentTop);
            Game.spriteBatch.draw(BitmapLoader.diamond, priceX - (GameValues.TITLE_HEIGHT * 0.7f), priceY + (GameValues.TITLE_HEIGHT * 0.26f), GameValues.TITLE_HEIGHT * 0.52f, GameValues.TITLE_HEIGHT * 0.46f);
            buyButton.draw(w - buyButton.w, priceY - (pad / 2) - (buyButton.h / 2) - GameValues.CORNER_SCALE, parentRight, parentTop);

            Game.spriteBatch.setShader(Game.shaderProgram);
            Game.spriteBatch.draw(region, relX + x + pad, relY + y + pad, GameValues.CAR_WIDTH, GameValues.CAR_HEIGHT);
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
