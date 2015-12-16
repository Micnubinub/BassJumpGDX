package tbs.bassjump.fragments;


import java.util.ArrayList;

import tbs.bassjump.utility.StoreItem;
import tbs.bassjump.view_lib.ListView;
import tbs.bassjump.view_lib.View;
import tbs.bassjump.view_lib.ViewGroup;

public class ColorFragment extends ListView {
    private static ListView listView;

    private static Adapter adapter;
    private static ArrayList<StoreItem> storeItems;

    public ColorFragment() {
    }

    public static void setStoreItems(ArrayList<StoreItem> storeItems) {
        ColorFragment.storeItems = storeItems;
        //Todo set scroll to 0
    }

    @Override
    public void dispose() {

    }

    @Override
    public void draw(float relX, float relY, float parentRight, float parentTop) {

    }

    public static class ListItem extends ViewGroup {
        public ListItem() {

        }

        @Override
        public void dispose() {

        }

        @Override
        public void draw(float relX, float relY, float parentRight, float parentTop) {

        }
    }

    public static class Adapter extends tbs.bassjump.view_lib.Adapter {
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
