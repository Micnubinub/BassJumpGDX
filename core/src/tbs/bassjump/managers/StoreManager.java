package tbs.bassjump.managers;


import tbs.bassjump.Utility;
import tbs.bassjump.ui.CustomDialog;
import tbs.bassjump.utility.GameUtils;
import tbs.bassjump.utility.ListViewLib;
import tbs.bassjump.utility.StoreItem;

public class StoreManager {
    // public static AdManager adManager;
    private static final ListViewLib.StoreListener storeListener = new ListViewLib.StoreListener() {
        @Override
        public boolean onBuyItem(StoreItem item) {
            final int coins = GameUtils.getCoins();
            if (item.bought) {
                this.onEquipItem(item);
                return false;
            } else if (coins < item.price) {
                this.onFailedToBuyItem(item);
                return false;
            } else {
                GameUtils.saveCoins(coins - item.price);
                switch (item.type) {
                    case COLOR:
                        GameUtils.addBoughtColors(item.tag);
                        break;

                    case SHAPE:
                        GameUtils.addBoughtShapes(item.tag);
                        break;
                }
                item.bought = true;
                CustomDialog.setNumCoins(coins - item.price);
            }
            return true;
        }

        @Override
        public void onEquipItem(StoreItem item) {
            switch (item.type) {
                case SHAPE:
                    GameUtils.equipShape(item.tag);
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

}
