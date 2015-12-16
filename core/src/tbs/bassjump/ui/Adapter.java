package tbs.bassjump.ui;


import java.util.ArrayList;

import tbs.bassjump.Utility;
import tbs.bassjump.utility.ListViewLib;
import tbs.bassjump.utility.StoreItem;
import tbs.bassjump.view_lib.Button;
import tbs.bassjump.view_lib.TextView;
import tbs.bassjump.view_lib.View;
import tbs.bassjump.view_lib.ViewGroup;

/**
 * Created by root on 4/01/15.
 */
public class Adapter extends tbs.bassjump.view_lib.Adapter {
    private static final StoreListItem holder = new StoreListItem();
    final ArrayList<StoreItem> storeItems;
    private View.OnClickListener storeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view, int x, int y) {
            final int pos = (Integer) view.getTag();
            if (ListViewLib.buyItem(storeItems.get(pos))) {
                storeItems.get(pos).bought = true;
            }
        }
    };

    public Adapter(ArrayList<StoreItem> storeItems) {
        this.storeItems = storeItems;
    }

    @Override
    public int getCount() {
        return storeItems.size();
    }

    @Override
    public Object getItem(int position) {
        return storeItems.get(position);
    }

    @Override
    public View getView(int position) {
//        holder.iconImageView.setVisibility(View.GONE);
        final StoreItem item = storeItems.get(position);
        StoreListItem.name.setText(item.name);
        StoreListItem.description.setText(item.description);

        StoreListItem.price.setText(" " + Utility.formatNumber(item.price)); // GAP
        StoreListItem.buy.setText(item.bought ? "Use" : "Buy");
        StoreListItem.price.setText(item.bought ? "Sold" : StoreListItem.price.getText());
//       Todo holder.price.setVisibility(item.bought ? View.GONE : View.VISIBLE);

//        buy_equip.setTag(R.id.buy_now, position);
        StoreListItem.buy_equip.setOnClickListener(storeClickListener);

        switch (item.type) {
            case SHAPE:
                StoreListItem.icon.addView(Utility.getShape(item.tag));
                break;
            case COLOR:
                StoreListItem.icon.addView(Utility.getColorView(item.tag));
                if (item.bought) {
                    if (item.tag.equals(Utility.COLOR_RED)) {
                        StoreListItem.buy_equip.setText("Added");
                        StoreListItem.buy_equip.setOnClickListener(null);
                    } else {
                        StoreListItem.buy_equip.setText(item.equipped ? "Remove" : "Use");
                        StoreListItem.buy_equip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view, int x, int y) {
                                if (item.equipped)
                                    Utility.removeEquippedColors(item.tag);
                                else
                                    Utility.addEquippedColors(item.tag);
                                item.equipped = !item.equipped;
                            }
                        });
                    }
                }
                break;

        }

        return holder;
    }

    public static class StoreListItem extends ViewGroup {
        public static TextView name, description, buy, price;
        public static Button buy_equip;
        public static ViewGroup icon;

        @Override
        public void dispose() {

        }

        @Override
        public void draw(float relX, float relY, float parentRight, float parentTop) {

        }
    }

//    public static final Background buttonBackground = new Background();


}
