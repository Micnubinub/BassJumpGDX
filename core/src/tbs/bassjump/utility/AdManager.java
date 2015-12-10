package tbs.bassjump.utility;


public class AdManager {
    private static AdView bannerAd;
    private static InterstitialAd fullscreenAd;
    private static InterstitialAd videoAd;

    public AdManager(Context context) {
        bannerAd = new AdView(context);
        bannerAd.setAdUnitId(context.getResources().getString(
                R.string.banner_id));

        fullscreenAd = new InterstitialAd(context);
        fullscreenAd.setAdUnitId(context.getResources().getString(
                R.string.fullscreen_id));
        fullscreenAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
                loadFullscreenAd(); // LOAD NEXT ONE

            }
        });

        videoAd = new InterstitialAd(context);
        videoAd.setAdUnitId(context.getResources().getString(R.string.video_id));
    }

    public void loadBannerAd() { // Get And Load Banner Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAd.setAdSize(AdSize.BANNER);
        bannerAd.setVisibility(View.VISIBLE);
        bannerAd.loadAd(adRequest);
    }

    public void loadFullscreenAd() { // Get And Load Fullscreen Ad
        try {
            AdRequest adRequest = new AdRequest.Builder().build();
            fullscreenAd.loadAd(adRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadVideoAd() { // Get And Load Video Ad
        AdRequest adRequest = new AdRequest.Builder().build();
        videoAd.loadAd(adRequest);
    }

    public AdView getBannerAd() {
        return bannerAd;
    }

    public InterstitialAd getFullscreenAd() {
        return fullscreenAd;
    }

    public InterstitialAd getVideoAd() {
        return videoAd;
    }
}
