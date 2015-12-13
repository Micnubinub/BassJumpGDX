package tbs.bassjump.ui;


import java.util.ArrayList;

import tbs.bassjump.utility.ListViewLib;
import tbs.bassjump.utility.StoreItem;

/**
 * Created by root on 4/01/15.
 */
public class Adapter extends BaseAdapter {
    final ArrayList<StoreItem> storeItems;
    private final View.OnClickListener storeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int pos = (Integer) v.getTag(R.id.buy_now);
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
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position) {
//        holder.iconImageView.setVisibility(View.GONE);

         holder.name.setMaxLines(1);

        final StoreItem item = storeItems.get(position);
        holder.name.setText(item.name);
        holder.description.setText(item.description);

        holder.price.setText(" " + Utility.formatNumber(item.price)); // GAP
        holder.buy.setText(item.bought ? "Use" : "Buy");
        holder.price.setText(item.bought ? "Sold" : holder.price.getText());
        holder.coinContainer.setVisibility(item.bought ? View.GONE : View.VISIBLE);

//        buy_equip.setTag(R.id.buy_now, position);
        buy_equip.setOnClickListener(storeClickListener);



        switch (item.type) {
            case SHAPE:
                holder.icon.addView(Utility.getShape(context, item.tag), params);
                break;
            case COLOR:
                holder.icon.addView(Utility.getColor(context, item.tag));
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
                                    Utility.removeEquippedColors(context, item.tag);
                                else
                                    Utility.addEquippedColors(context, item.tag);
                                item.equipped = !item.equipped;


                            }
                        });
                    }
                }
                break;

        }

        return convertView;
    }


}
