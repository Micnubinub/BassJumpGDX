package tbs.bassjump.ui;


import java.util.ArrayList;

import tbs.bassjump.Utility;
import tbs.bassjump.fragments.ColorFragment;
import tbs.bassjump.fragments.ShapeFragment;
import tbs.bassjump.view_lib.ListView;
import tbs.bassjump.view_lib.TextView;
import tbs.bassjump.view_lib.View;
import tbs.bassjump.view_lib.ViewGroup;
import tbs.bassjump.view_lib.ViewPager;

/**
 * Created by root on 4/01/15.
 */
public class CustomDialog extends ViewGroup {
    private static final View[] fragments = new ListView[2];
    private static TextView title;
    private static TextView coinText;
    private static ViewGroup tabs;
    private static ViewPager pager;
    //    private static MyPagerAdapter pagerAdapter;
    private static boolean show;
    private final String[] TITLES = {"Colors", "Shapes"};

    public CustomDialog() {
        setUpFragments();
    }

    public static void setNumCoins(int numCoins) {
//        if (numCoins >= 10000) {
//            MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQEA");
//        } else if (numCoins >= 1000) {
//            MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQCw");
//        }
        try {
            coinText.setText(Utility.formatNumber(numCoins));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void draw(float relX, float relY, float parentRight, float parentTop) {
        if (!show)
            return;
// todo        view.findViewById(R.id.close).setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dismiss();
//                    }
//                });

        setNumCoins(Utility.getCoins());
    }

    public void dismiss() {
        show = false;
    }

    private void setUpFragments() {
        fragments[0] = new ColorFragment();
        fragments[1] = new ShapeFragment();

        ColorFragment.setStoreItems(Utility
                .getColorStoreItems());
        ShapeFragment.setStoreItems(Utility
                .getShapeStoreItems());

        title = new TextView("Store", (int) w / 2);

        coinText = new TextView((int) w / 2);


        tabs = new ViewGroup() {
            @Override
            public void dispose() {

            }

            @Override
            public void draw(float relX, float relY, float parentRight, float parentTop) {

            }
        };
        pager = new ViewPager() {
            @Override
            public ArrayList getTitles() {
                return null;
            }
        };

//    todo    tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
//        pager = (ViewPager) view.findViewById(R.id.view_pager);
//
//        pagerAdapter = new MyPagerAdapter(getChildFragmentManager());

    }

}
