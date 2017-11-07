package com.rudanic.earthquaketracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;



public class FactsEarthquakeFragment extends Fragment {

    @BindView(R.id.expandableButton1)
    Button button1;
    View view;

    ExpandableRelativeLayout expandableLayout1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_fact_earthquake, container, false);

        ButterKnife.bind(this, view);

        /**Set the AdView*/
        /**Load an ad into the AdMob banner view. */
        MobileAds.initialize(getActivity(), String.valueOf(R.string.banner_ad_unit_id));

        AdView adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

        expandableLayout1 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout1.toggle(); // toggle expand and collapse
            }
        });

        return view;
    }
}


