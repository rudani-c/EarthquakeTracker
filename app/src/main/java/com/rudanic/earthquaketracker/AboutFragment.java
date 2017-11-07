package com.rudanic.earthquaketracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class AboutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frabment_about, container, false);

        TextView touchToOpenLicences = (TextView) view.findViewById(R.id.tv_touch_for_licences);
        final TextView licences = (TextView) view.findViewById(R.id.sources_licence);

        touchToOpenLicences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                licences.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}
