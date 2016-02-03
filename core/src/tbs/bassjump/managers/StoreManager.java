package tbs.bassjump.managers;


import tbs.bassjump.Utility;
import tbs.bassjump.utility.ListViewLib;
import tbs.bassjump.utility.StoreItem;
import tbs.bassjump.view_lib.ViewPager;

public class StoreManager {
    // public static AdManager adManager;
    private static final ListViewLib.StoreListener storeListener = new ListViewLib.StoreListener() {
        @Override
        public boolean onBuyItem(StoreItem item) {
            final int coins = Utility.getCoins();
            if (item.bought) {
                this.onEquipItem(item);
                return false;
            } else if (coins < item.price) {
                this.onFailedToBuyItem(item);
                return false;
            } else {
                Utility.saveCoins(coins - item.price);
                switch (item.type) {
                    case COLOR:
                        Utility.addBoughtColors(item.tag);
                        break;

                    case SHAPE:
                        Utility.addBoughtShapes(item.tag);
                        break;
                }
                item.bought = true;
                ViewPager.setNumCoins(coins - item.price);
            }
            return true;
        }

        @Override
        public void onEquipItem(StoreItem item) {
            switch (item.type) {
                case SHAPE:
                    Utility.equipShape(item.tag);
                    break;
            }
        }

        @Override
        public void onFailedToBuyItem(StoreItem item) {
            Utility.print("You don't have enough money");
        }

        @Override
        public void onStoreOpened() {
            try {
//       Todo         adManager.loadFullscreenAd();
//       Todo         adManager.loadVideoAd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStoreClosed() {
        }
    };

    private static ListViewLib listViewLib;

    public StoreManager() {
        listViewLib = new ListViewLib();
        listViewLib.setStoreListener(storeListener);
    }

    public void showStore() {
        listViewLib.show();
    }

    //Todo implement
//    public static void setTextureColor(int color) {
//        dispose();
//        final Texture tmpTexture = new Texture(Gdx.files.internal("sprites"));
//        final TextureData data = tmpTexture.getTextureData();
//        data.prepare();
//        while (!data.isPrepared()) {
//
//        }
//        final Pixmap p = data.consumePixmap();
//        int j, w = p.getWidth(), h = p.getHeight(), total = w * h;
//        for (int i = 0; i < total; i++) {
//            j = i % w;
//            if (0x000000ff == p.getPixel(i, j)) {
//                p.drawPixel(i, j, color);
//            }
//        }
//
//        regionTexture = new Texture(p);
//
////        data.disposePixmap();
//        tmpTexture.dispose();
////        p.dispose();
//        region.setTexture(regionTexture);
//    }

}
