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



public class AboutEarthquakeFragment extends Fragment {

    @BindView(R.id.expandableButton1)
    Button button1;
    @BindView(R.id.expandableButton2)
    Button button2;
    @BindView(R.id.expandableButton3)
    Button button3;
    @BindView(R.id.expandableButton4)
    Button button4;
    @BindView(R.id.expandableButton5)
    Button button5;
    @BindView(R.id.expandableButton6)
    Button button6;
    @BindView(R.id.expandableButton7)
    Button button7;
    @BindView(R.id.expandableButton8)
    Button button8;
    View view;

    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5, expandableLayout6, expandableLayout7, expandableLayout8;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_about_earthquake, container, false);

        ButterKnife.bind(this, view);

        /**Set the AdView*/
        /**Load an ad into the AdMob banner view. */
        MobileAds.initialize(getActivity(), String.valueOf(R.string.banner_ad_unit_id));

        AdView adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

        expandableLayout1 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout1);
        expandableLayout2 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout2);
        expandableLayout3 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout3);
        expandableLayout4 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout4);
        expandableLayout5 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout5);
        expandableLayout6 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout6);
        expandableLayout7 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout7);
        expandableLayout8 = (ExpandableRelativeLayout) view.findViewById(R.id.expandableLayout8);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout1.toggle(); // toggle expand and collapse
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout2.toggle(); // toggle expand and collapse
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout3.toggle(); // toggle expand and collapse
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout4.toggle(); // toggle expand and collapse
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout5.toggle(); // toggle expand and collapse
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout6.toggle(); // toggle expand and collapse
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout7.toggle(); // toggle expand and collapse
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLayout8.toggle(); // toggle expand and collapse
            }
        });

        return view;
    }
}
