package tbs.bassjump.ui;


/**
 * Created by root on 4/01/15.
 */
public class CustomDialog extends DialogFragment {
    private static final Fragment[] fragments = new Fragment[4];
    private static TextView title;
    private static TextView coinText;
    private static PagerSlidingTabStrip tabs;
    private static ViewPager pager;
    private static MyPagerAdapter pagerAdapter;
    private View view;

    public CustomDialog() {
        super();
    }

    public static void setNumCoins(int numCoins) {
        if (numCoins >= 10000) {
            MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQEA");
        } else if (numCoins >= 1000) {
            MainActivity.unlockAchievement("CgkIvYbi1pMMEAIQCw");
        }
        try {
            coinText.setText(Utility.formatNumber(numCoins));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.store, null);
        title = (TextView) view.findViewById(R.id.title);
        title.setTypeface(Game.font);

        coinText = (TextView) view.findViewById(R.id.coins);
        coinText.setTypeface(Game.font);

        view.findViewById(R.id.close).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        setNumCoins(Utility.getCoins(Game.context));
        return view;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        return super.show(transaction, tag);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpFragments();
    }

    private void setUpFragments() {
        fragments[0] = new ColorFragment();
        fragments[1] = new ShapeFragment();
        fragments[2] = new MusicFragment();
        fragments[3] = MainActivity.getCoinsFragment;

        ColorFragment.setListAdapter(new Adapter(Game.context, Utility
                .getColorStoreItems(Game.context)));
        ShapeFragment.setListAdapter(new Adapter(Game.context, Utility
                .getShapeStoreItems(Game.context)));
        MusicFragment.setListAdapter(new Adapter(Game.context, Utility
                .getSongStoreItems()));

        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        pager = (ViewPager) view.findViewById(R.id.view_pager);
        pager.setOffscreenPageLimit(4);

        pagerAdapter = new MyPagerAdapter(getChildFragmentManager());

        pager.setAdapter(pagerAdapter);

        final int pageMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, Game.context.getResources()
                        .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);

        // Toast.makeText(tbs.jumpsnew.tbs.bassjump.reference.Game.tbs.bassjump.reference.Game.context,
        // "Listing song in the Music or music folder", Toast.LENGTH_LONG)
        // .show();

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Colors", "Shapes", "Music",
                "Get Coins"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }
    }

}
