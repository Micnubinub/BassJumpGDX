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
                holder.icon.addView(Utility.getShape(item.tag));
                break;
            case COLOR:
                holder.icon.addView(Utility.getColor(item.tag));
                if (item.bought) {
                    if (item.tag.equals(Utility.COLOR_RED)) {
                        button.setText("Added");
                        button.setOnClickListener(null);
                    } else {
                        button.setText(item.equipped ? "Remove" : "Use");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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

        @Override
        public void dispose() {

        }

        @Override
        public void draw(float relX, float relY, float parentRight, float parentTop) {

        }
    }

//    public static final Background buttonBackground = new Background();


}
