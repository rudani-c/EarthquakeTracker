package com.rudanic.earthquaketracker.model;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeNews  {
    private String type;
    private EarthquakeNewsMetadata metadata;
    private List<EarthquakeNewsFeatures> features = new ArrayList<>();
    private List<Double> bbox = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EarthquakeNewsMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(EarthquakeNewsMetadata metadata) {
        this.metadata = metadata;
    }

    public List<EarthquakeNewsFeatures> getFeatures() {
        return features;
    }

    public void setFeatures(List<EarthquakeNewsFeatures> features) {
        this.features = features;
    }

    public List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }
}
