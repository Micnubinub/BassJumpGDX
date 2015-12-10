package tbs.bassjump.fragments;


import tbs.bassjump.Game;
import tbs.bassjump.ui.CustomDialog;
import tbs.bassjump.utility.GameUtils;

public class GetCoinsFragment extends Fragment {
    private static final AdListener fullScreenListener = new AdListener() {
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            fullClicked = false;
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            GameUtils.saveCoins(GameUtils.getCoins() + 50);
            CustomDialog.setNumCoins(GameUtils.getCoins());
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            if (fullClicked)
                adManager.getFullscreenAd().show();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            fullClicked = false;
            toast("Failed to load Ad");
        }
    };
    public static AdManager adManager;
    private static boolean vidClicked, fullClicked;
    private static final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.video_ad:
                    vidClicked = true;
                    if (adManager.getVideoAd().isLoaded())
                        adManager.getVideoAd().show();
                    else
                        adManager.loadVideoAd();
                    break;
                case R.id.fullscreen_ad:
                    fullClicked = true;
                    if (adManager.getFullscreenAd().isLoaded())
                        adManager.getFullscreenAd().show();
                    else
                        adManager.loadFullscreenAd();
                    break;
                case R.id.iap1:
                    MainActivity.purchases.makePurchase(GameValues.IAP_1_ID);
                    break;
                case R.id.iap2:
                    MainActivity.purchases.makePurchase(GameValues.IAP_2_ID);
                    break;
                case R.id.iap3:
                    MainActivity.purchases.makePurchase(GameValues.IAP_3_ID);
                    break;
                case R.id.iap4:
                    MainActivity.purchases.makePurchase(GameValues.IAP_4_ID);
                    break;
                case R.id.iap5:
                    MainActivity.purchases.makePurchase(GameValues.IAP_5_ID);
                    break;
            }
        }
    };
    private static final AdListener videoListener = new AdListener() {
        @Override
        public void onAdClosed() {
            super.onAdClosed();
            vidClicked = false;
        }

        @Override
        public void onAdOpened() {
            super.onAdOpened();
            Utility.saveCoins(Game.context,
                    Utility.getCoins(Game.context) + 100);
            CustomDialog.setNumCoins(Utility.getCoins(Game.context));
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            if (vidClicked)
                adManager.getVideoAd().show();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
            vidClicked = false;
            toast("Failed to load Ad");
        }
    };

    public GetCoinsFragment() {

    }

    private static void toast(String msg) {
        Toast.makeText(Game.context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adManager = new AdManager(getActivity().getApplicationContext());
        adManager.getVideoAd().setAdListener(videoListener);
        adManager.getFullscreenAd().setAdListener(fullScreenListener);
        adManager.loadFullscreenAd();
        adManager.loadVideoAd();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater
                .inflate(R.layout.get_coins, container, false);
        view.findViewById(R.id.iap1).setOnClickListener(listener);
        view.findViewById(R.id.iap2).setOnClickListener(listener);
        view.findViewById(R.id.iap3).setOnClickListener(listener);
        view.findViewById(R.id.iap4).setOnClickListener(listener);
        view.findViewById(R.id.iap5).setOnClickListener(listener);
        view.findViewById(R.id.video_ad).setOnClickListener(listener);
        view.findViewById(R.id.fullscreen_ad).setOnClickListener(listener);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.getCoinsFragment = new GetCoinsFragment();
    }
}
