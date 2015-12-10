package tbs.bassjump.utility;


import tbs.bassjump.ui.CustomDialog;

/**
 * Created by root on 29/12/14.
 */
public class ListViewLib {

    private static StoreListener storeListener;
    // private static int numCoins;
    // , descriptionTextSize, priceTextSize, itemNameTextSize,buyButtonTextSize;

    // private static Drawable listItemBackground;
//    private static View view;
    private CustomDialog dialog;

    //    private int nameTextColor, priceTextColor, itemTextColor, buyTextColor,
//            descriptionTextColor, coinTextColor, titleTextColor;
//    private int listViewDividerHeight, listViewPadding, listViewMargin;
//    private Drawable storeItemBackground;
//    private Typeface storeItemFont;
//    private Drawable buyButtonBackground;


    public static boolean buyItem(StoreItem item) {
        return storeListener.onBuyItem(item);
    }


//    public void setItemNameTextSize(int itemNameTextSize) {
//        this.itemNameTextSize = itemNameTextSize;
//    }
//
//    public void setDescriptionTextSize(int descriptionTextSize) {
//        this.descriptionTextSize = descriptionTextSize;
//    }
//
//    public void setBuyButtonTextSize(int buyButtonTextSize) {
//        this.buyButtonTextSize = buyButtonTextSize;
//    }

    public void show() {
        if (dialog == null) {
            dialog = new CustomDialog();
        }
//Todo show dialog here
        if (storeListener != null)
            storeListener.onStoreOpened();
    }


    public void setStoreListener(StoreListener storeListener) {
        this.storeListener = storeListener;
    }

//    public void setListItemBackground(Drawable listItemBackground) {
//        this.listItemBackground = listItemBackground;
//    }
//
//    public void setStoreItemBackground(Drawable storeItemBackground) {
//        this.storeItemBackground = storeItemBackground;
//    }
//
//    private void setStoreItemFont(Typeface typeface) {
//        storeItemFont = typeface;
//    }
//
//    public void setNameTextColor(int nameTextColor) {
//        this.nameTextColor = nameTextColor;
//    }
//
//    public void setPriceTextColor(int priceTextColor) {
//        this.priceTextColor = priceTextColor;
//    }
//
//    public void setDescriptionTextColor(int descriptionTextColor) {
//        this.descriptionTextColor = descriptionTextColor;
//    }
//
//    public void setCoinTextColor(int coinTextColor) {
//        this.coinTextColor = coinTextColor;
//    }
//
//    public void setTitleTextColor(int titleTextColor) {
//        this.titleTextColor = titleTextColor;
//    }
//
//    public void setTitle(String title) {
//        this.title.setText(title);
//    }
//
//    public void setPriceTextSize(int priceTextSize) {
//        this.priceTextSize = priceTextSize;
//    }
//
//    public void setBuyButtonBackground(Drawable buyButtonBackground) {
//        this.buyButtonBackground = buyButtonBackground;
//    }

    public interface StoreListener {
        boolean onBuyItem(StoreItem item);

        void onEquipItem(StoreItem item);

        void onFailedToBuyItem(StoreItem item);

        void onStoreOpened();

        void onStoreClosed();
    }


}
