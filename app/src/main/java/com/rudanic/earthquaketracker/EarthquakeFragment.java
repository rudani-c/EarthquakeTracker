package com.rudanic.earthquaketracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rudanic.earthquaketracker.adapter.EarthquakeAdapter;
import com.rudanic.earthquaketracker.api.EarthquakeNewsApi;
import com.rudanic.earthquaketracker.model.EarthquakeNews;
import com.rudanic.earthquaketracker.model.EarthquakeNewsFeatures;
import com.rudanic.earthquaketracker.model.EarthquakeNewsFeaturesProperties;
import com.rudanic.earthquaketracker.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EarthquakeFragment extends Fragment implements Callback<EarthquakeNews> {

    public static final String LOG_TAG = MainActivity.class.getName();
    final String BACKGROUND_URL = "http://www.missionmission.org/wp-content/uploads/2014/12/rain-mission.gif";
    RecyclerView earthquakeRecyclerView;
    private EarthquakeAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    TextView emptyView;
    ImageView backgroundImage;
    View loadingIndicator;
    SwipeRefreshLayout mSwipeRefreshLayout;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_earthquake, container, false);

        setViews();
        setAdapter();
        checkNetworkAvaibility(container);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        return view;
    }

    private void setViews() {

        emptyView = (TextView) view.findViewById(R.id.empty_list_item);
        emptyView.setVisibility(View.GONE);

        YoYo.with(Techniques.ZoomInRight)
                .duration(900)
                .playOn(view.findViewById(R.id.tv_company_name));

        backgroundImage = (ImageView) view.findViewById(R.id.iv_background);
        Glide.with(this).load(BACKGROUND_URL).into(backgroundImage);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        loadingIndicator = view.findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.VISIBLE);

        /**Set the AdView*/
        /**Load an ad into the AdMob banner view.*/
        MobileAds.initialize(getActivity(), String.valueOf(R.string.banner_ad_unit_id));

        AdView adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

        // Find a reference to the {@link ListView} in the layout
        earthquakeRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        earthquakeRecyclerView.setHasFixedSize(true);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth / 400);

        gridLayoutManager = new GridLayoutManager(getActivity(), columns);
        earthquakeRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void setAdapter() {
        // set adapter using animation library
        adapter = new EarthquakeAdapter(getActivity(), new ArrayList<EarthquakeNewsFeaturesProperties>());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
        alphaAdapter.setDuration(250);
        alphaAdapter.setInterpolator(new OvershootInterpolator());
        earthquakeRecyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
    }

    private void checkNetworkAvaibility(@Nullable ViewGroup container) {
        // Get a reference to the LoaderManager, in order to interact with loaders.
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(LOG_TAG, "2");
            asyncLibraryClass();
            Log.i(LOG_TAG, "3....");
        } else {
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("Please check your internet connection...");       //MOVE TO STRING RESOURCE
            Snackbar.make(container, "No Internet Connection Found...\nSwipe down screen once you got internet", 5000).show();
        }
    }

    private void asyncLibraryClass() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EarthquakeNewsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(Assets.filter){

            SharedPreferences sharedPref = getActivity().getSharedPreferences(Assets.SHARED_PREF_NAME, getActivity().MODE_PRIVATE);
            String latitude = sharedPref.getString("latitude","20");
            String longitude = sharedPref.getString("longitude","20");
            String radius = sharedPref.getString("radius","5");
            EarthquakeNewsApi earthquakeNewsApi = retrofit.create(EarthquakeNewsApi.class);
            Call<EarthquakeNews> earthquakeListCall = earthquakeNewsApi.getFilteredEarthquakes("geojson", "100", latitude, longitude, radius);
            earthquakeListCall.enqueue(this);
            Assets.filter = false;

        }else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String minMagnitude = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));
            String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));

            EarthquakeNewsApi earthquakeNewsApi = retrofit.create(EarthquakeNewsApi.class);
            Call<EarthquakeNews> earthquakeListCall = earthquakeNewsApi.getEarthquakeNews("geojson", "100", minMagnitude, orderBy);
            earthquakeListCall.enqueue(this);
        }


    }

    @Override
    public void onResponse(Call<EarthquakeNews> call, Response<EarthquakeNews> response) {
        if (response.isSuccessful()) {
            EarthquakeNews earthquakeNews = response.body();
            onObjectLoaded(earthquakeNews);
        }
    }

    @Override
    public void onFailure(Call<EarthquakeNews> call, Throwable t) {
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setText("Failed to load latest news");
    }

    private void onObjectLoaded(EarthquakeNews earthquakeNews) {
        List<EarthquakeNewsFeatures> features = earthquakeNews.getFeatures();
        List<EarthquakeNewsFeaturesProperties> featuresPropertiesList = new ArrayList<>();
        loadingIndicator.setVisibility(View.GONE);

        if (!features.isEmpty()) {
            adapter.notifyItemRangeRemoved(1, features.size());

            for (EarthquakeNewsFeatures feature : features) {
                EarthquakeNewsFeaturesProperties properties = feature.getProperties();

                featuresPropertiesList.add(properties);
            }

            emptyView.setVisibility(View.GONE);
            adapter.setCardInfoList(featuresPropertiesList);
            adapter.notifyDataSetChanged();
        } else {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No Earthquake Found...");
        }
    }

    public void refreshItems() {
        asyncLibraryClass();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
