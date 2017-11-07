package com.rudanic.earthquaketracker.adapter;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.rudanic.earthquaketracker.R;
import com.rudanic.earthquaketracker.model.EarthquakeNewsFeaturesProperties;
import com.rudanic.earthquaketracker.ui.WebActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.ViewHolder> {
    Context cont;
    String mTsunamiMsg;
    List<EarthquakeNewsFeaturesProperties> earthquakeList;
    String location, distance;
    String stringDate;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView distanceView;
        private TextView locationView;
        private TextView dateView;
        private TextView magnitudeView;
        private TextView share;
        private TextView tsunamiView;
        private TextView alertView;

        public ViewHolder(View itemView) {
            super(itemView);
            distanceView = (TextView) itemView.findViewById(R.id.text_dis);
            locationView = (TextView) itemView.findViewById(R.id.text_city);
            dateView = (TextView) itemView.findViewById(R.id.text_date);
            magnitudeView = (TextView) itemView.findViewById(R.id.text_mag);
            share = (TextView) itemView.findViewById(R.id.share);
            tsunamiView = (TextView) itemView.findViewById(R.id.text_tsuanmi);
            alertView = (TextView) itemView.findViewById(R.id.text_impact);
            view = itemView;
        }
    }

    public EarthquakeAdapter(Context context, List<EarthquakeNewsFeaturesProperties> earthquakes) {
        cont = context;
        this.earthquakeList = earthquakes;
        Log.i("Adapter constructor", "11");
    }

    @Override
    public EarthquakeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.earthquake_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EarthquakeNewsFeaturesProperties earthquake = earthquakeList.get(position);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US);
        Date date = new Date(earthquake.getTime());
        stringDate = dateFormatter.format(date);

        String mLoc = earthquake.getPlace();
        if (mLoc.contains("of")) {
            String[] parts = mLoc.split("(?<=of )");
            distance = parts[0];
            location = parts[1];
        } else {
            distance = "Near the";
            location = mLoc;
        }

        holder.dateView.setText(stringDate);
        holder.distanceView.setText(distance);
        holder.locationView.setText(location);
        holder.magnitudeView.setText(String.valueOf(earthquake.getMag()));

        /** To set Tsunami Alert Text */
        if (earthquake.getTsunami() == 1) {
            holder.tsunamiView.setVisibility(View.VISIBLE);
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(500); //You can manage the blinking time with this parameter
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            holder.tsunamiView.startAnimation(anim);
        } else {
            holder.tsunamiView.setVisibility(View.GONE);
        }

        /** To set Impact color and text */
        if (earthquake.getAlert() == null) {
            holder.alertView.setVisibility(View.GONE);
        } else {
            holder.alertView.setVisibility(View.VISIBLE);
            switch (earthquake.getAlert()) {
                case "green":
                    holder.alertView.setTextColor(cont.getResources().getColor(R.color.green));
                    break;
                case "yellow":
                    holder.alertView.setTextColor(cont.getResources().getColor(R.color.yellow));
                    break;
                case "orange":
                    holder.alertView.setTextColor(cont.getResources().getColor(R.color.orange));
                    break;
                case "red":
                    holder.alertView.setTextColor(cont.getResources().getColor(R.color.red));
                    break;
            }
        }

        /** To set Circle color */
        GradientDrawable magnitudeCircle = (GradientDrawable) holder.magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(earthquake.getMag());
        magnitudeCircle.setColor(magnitudeColor);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int finalRadius = (int)Math.hypot(v.getWidth()/2, v.getHeight()/2);
                Animator anim = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    anim = ViewAnimationUtils.createCircularReveal(v, (int) v.getWidth()/2, (int) v.getHeight()/2, 0, finalRadius);
                }
                v.setBackgroundColor(v.getContext().getResources().getColor(R.color.circle_effect_blue));
                anim.start();

                Intent webIntent = new Intent(cont, WebActivity.class);
                webIntent.putExtra("url", earthquake.getUrl());
                cont.startActivity(webIntent);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (earthquake.getTsunami() == 1) {
                    mTsunamiMsg =  cont.getString(R.string.tsunami_alert) + "\n";
                } else {
                    mTsunamiMsg = cont.getString(R.string.tsunami_alert_null);
                }
                Intent shareIntent = new Intent();
                shareIntent.setType("text/plain");
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, mTsunamiMsg
                        + cont.getString(R.string.earthquake_of_magnitude)
                        + String.valueOf(earthquake.getMag())
                        + cont.getString(R.string.found_at)
                        + distance
                        + location
                        + cont.getString(R.string.on)
                        + stringDate);

                cont.startActivity(Intent.createChooser(shareIntent, cont.getResources().getString(R.string.share_to)));

            }
        });
    }

    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }

    public void setCardInfoList(List<EarthquakeNewsFeaturesProperties> earthquakes) {
        this.earthquakeList = earthquakes;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(cont, magnitudeColorResourceId);
    }
}

